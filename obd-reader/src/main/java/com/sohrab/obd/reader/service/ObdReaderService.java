/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sohrab.obd.reader.service;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.constants.DefineObdReader;
import com.sohrab.obd.reader.enums.ObdProtocols;
import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.ObdConfiguration;
import com.sohrab.obd.reader.obdCommand.control.TroubleCodesCommand;
import com.sohrab.obd.reader.obdCommand.protocol.EchoOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.LineFeedOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.ObdResetCommand;
import com.sohrab.obd.reader.obdCommand.protocol.SelectProtocolCommand;
import com.sohrab.obd.reader.obdCommand.protocol.SpacesOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.TimeoutCommand;
import com.sohrab.obd.reader.trip.TripRecord;
import com.sohrab.obd.reader.utils.L;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import app.com.android_obd_reader.R;


/**
 * created by sohrab 01/12/2017
 * <p>
 * Service for managing connection and data communication with a OBD-2 in background and update data to RealTime screen.
 * It connects paired OBD-2 Or wait until paired.
 * Once it is paired, try to connect with Bluetooth Socket along with some specific OBD-2 command,
 * if connected, fetch data until OBD-2 disconnected and if somehow,
 * it disconnected then go to connect and this is in loop until user quit from App.
 */

public class ObdReaderService extends IntentService implements DefineObdReader {
    private static final String TAG = "ObdReaderService";
    // receive when OBD-2 connected


    public final static char PID_STATUS_SUCCESS = '1';
    public final static int DEVICE_NOT_PAIRED = 1;
    public final static int OBD_NOT_RESPONDING = 2;
    public final static int OBD_CONNECTED = 3;
    public final static int INIT_OBD = 4;


    //   private static final int NOTIFICATION_ID = 101;
    private static final int DELAY_FIFTEEN_SECOND = 15000;
    private static final int DELAY_TWO_SECOND = 2000;
    // this is used to find TroubleCode if true. This is used in InspectionActivity where fault is shown.
    public boolean mIsFaultCodeRead = true;
    private final IBinder mBinder = new LocalBinder();
    //   private int mLastNotificationType;
    // name of OBD
    private String OBD_SMALL = "obd";
    private String OBD_CAPS = "OBD";
    private String V_LINK = "V-LINK";
    private BluetoothManager mBluetoothManager;//Bluetooth Manager
    private BluetoothAdapter mBluetoothAdapter;//Bluetooth adapter
    private BluetoothSocket mSocket;
    //set OBD-2 connection status
    private boolean isConnected;
    // private NotificationCompat.Builder mNotificationBuilder;
    //  private NotificationManager mNotificationManager;
    private boolean mIsRunningSuccess;
    private Intent mIntent = new Intent(ACTION_READ_OBD_REAL_TIME_DATA);
    private char[] mSupportedPids;

    public ObdReaderService() {
        super("ObdReaderService");
        L.i("ObdReaderService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        L.i("onHandleIntent" + "Thread is :: " + Thread.currentThread().getId());

        // setUpAsForeground();
        if (initiateConnection()) {

            if (!isEnable()) {
                enableBlutooth();
            }

            findObdDevicesAndConnect();
        }

        L.i("onHandleIntent bottom");
        //  mNotificationManager.cancel(NOTIFICATION_ID);
        ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(false);
        ObdPreferences.get(getApplicationContext()).setIsOBDconnected(false);
        TripRecord.getTripRecode(this).clear();
    }

    /**
     * This method is recursively called until service stopped.
     */
    private void findObdDevicesAndConnect() {

        if (!isConnected) {
            findPairedDevices();
        }

        if (isConnected) {
            executeCommand();
        }

        if (ObdPreferences.get(getApplicationContext()).getServiceRunningStatus()) {
            L.i("findObdDevicesAndConnect()");
            findObdDevicesAndConnect();
        }

    }

    /**
     * find paired OBD-2 devices in loop until found and connected or service stopped.
     */
    private void findPairedDevices() {

        while (!isConnected && ObdPreferences.get(getApplicationContext()).getServiceRunningStatus()) {
            if (mBluetoothAdapter != null) {
                boolean deviceFound = false;

                Set<BluetoothDevice> bluetoothDevices = mBluetoothAdapter.getBondedDevices();
                for (BluetoothDevice device : bluetoothDevices) {
                    if (device != null) {
                        String name = device.getName();
                        if (name != null && (name.contains(OBD_SMALL) || name.contains(OBD_CAPS) || name.toUpperCase().contains(V_LINK))) {
                            try {
                                connectOBDDevice(device);
                            } catch (Exception e) {
                                L.i("connectOBDDevice return Exception :: " + e != null ? e.getMessage() : "");
                            }
                            deviceFound = true;
                            break;
                        }
                    }

                }

                if (!deviceFound) {
                  /*  if (mLastNotificationType != DEVICE_NOT_PAIRED) {
                        mLastNotificationType = DEVICE_NOT_PAIRED;
                        updateNotification(getString(R.string.waiting_for_obd));
                    }*/
                    sendBroadcast(ACTION_OBD_CONNECTION_STATUS, getString(R.string.waiting_for_obd));
                }
            }
        }
    }

    /**
     * connects specified bluetooth OBD device with Bluetooth Socket.
     * if bluetooth socked connected then use some init OBD-2 command to initialize,
     * if command response is success, then we assume connection is established and ready to fetch data.
     *
     * @param device
     * @throws Exception
     */
    public void connectOBDDevice(final BluetoothDevice device) throws Exception {

        try {
            mSocket = (BluetoothSocket) device.getClass().getMethod("createInsecureRfcommSocket", new Class[]{int.class}).invoke(device, 1);
        } catch (Exception e) {
            // e.printStackTrace();
            L.i("createInsecureRfcommSocket failed");
            closeSocket();
        }

        if (mSocket != null) {
            try {
                mBluetoothAdapter.cancelDiscovery();
                Thread.sleep(500);
                mSocket.connect();
                L.i("Socket connected");
            } catch (Exception e) {
                L.i("Socket connection  exception :: " + e.getMessage());
                //   e.printStackTrace();
                closeSocket();
            }

            boolean isSockedConnected = mSocket.isConnected();
            if (isSockedConnected) {
                try {
                    Thread.sleep(DELAY_TWO_SECOND);
                  /*  if (mLastNotificationType != INIT_OBD) {
                        mLastNotificationType = INIT_OBD;
                        updateNotification(getString(R.string.connecting_to_ecu));
                    }*/
                    L.i("Executing reset command in new Thread :: " + Thread.currentThread().getId());
                    final Thread newThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                // this thread is required because in Headunit command.run method block infinitly ,
                                // therefore this thread life is maximum 15 second so that block can be handled.
                                mIsRunningSuccess = false;
                                new ObdResetCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                                Thread.sleep(1000);
                                new EchoOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                                Thread.sleep(200);
                                new LineFeedOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                                Thread.sleep(200);
                                new SpacesOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                                Thread.sleep(200);
                                new SpacesOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                                Thread.sleep(200);
                                new TimeoutCommand(125).run(mSocket.getInputStream(), mSocket.getOutputStream());
                                //  updateNotification(getString(R.string.searching_protocol));
                                Thread.sleep(200);
                                new SelectProtocolCommand(ObdProtocols.AUTO).run(mSocket.getInputStream(), mSocket.getOutputStream());
                                Thread.sleep(200);
                                new EchoOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                                //  updateNotification(getString(R.string.searching_supported_sensor));
                                Thread.sleep(200);
                                mIsRunningSuccess = true;
                                // checkPid0To20(true);

                            } catch (Exception e) {
                                mIsRunningSuccess = false;
                                L.i("In new thread reset command  exception :: " + e != null ? e.getMessage() : "");
                            }

                        }
                    });

                    newThread.start();
                    newThread.join(DELAY_FIFTEEN_SECOND);
                    L.i("Thread wake to check reset command status  i.e  :: " + Thread.currentThread().getId() + ",  mIsRunningSuccess :: " + mIsRunningSuccess);
                    isSockedConnected = mIsRunningSuccess;

                } catch (Exception e) {
                    L.i(" reset command Exception  :: " + e.getMessage());
                    isSockedConnected = false;
                }

            }

            if (mSocket != null && mSocket.isConnected() && isSockedConnected) {
                setConnection(false);
               /* if (mLastNotificationType != OBD_CONNECTED) {
                    mLastNotificationType = OBD_CONNECTED;
                    updateNotification(getString(R.string.connected_ok));
                }
*/
            } else {
                if (mSupportedPids != null && mSupportedPids.length == 32) {

                    if ((mSupportedPids[12] != PID_STATUS_SUCCESS) || (mSupportedPids[11] != PID_STATUS_SUCCESS)) {
                        // speed pid not supportedsupported
                        // updateNotification(getString(R.string.unable_to_connect));
                        sendBroadcast(ACTION_OBD_CONNECTION_STATUS, getString(R.string.unable_to_connect));
                        return;
                    }
                }

                sendBroadcast(ACTION_OBD_CONNECTION_STATUS, getString(R.string.obd2_adapter_not_responding));
/*
                if (mLastNotificationType != OBD_NOT_RESPONDING) {
                    mLastNotificationType = OBD_NOT_RESPONDING;
                    updateNotification(getString(R.string.obd2_adapter_not_responding));
                }
*/
            }
        }

    }


    /**
     * Once OBD-2 connected, this method will execute to fetch data continuously until OBD disconnected or Service stopped.
     */
    private void executeCommand() {
        L.i("executing commands thread is :: " + Thread.currentThread().getId());
        TripRecord tripRecord = TripRecord.getTripRecode(this);
        ArrayList<ObdCommand> commands = (ArrayList<ObdCommand>) ObdConfiguration.getmObdCommands().clone();
        int count = 0;
        while (mSocket != null && mSocket.isConnected() && commands.size() > count && isConnected && ObdPreferences.get(getApplicationContext()).getServiceRunningStatus()) {

            ObdCommand command = commands.get(count);
            try {

                L.i("command run :: " + command.getName());
                command.run(mSocket.getInputStream(), mSocket.getOutputStream());
                L.i("result is :: " + command.getFormattedResult() + " :: name is :: " + command.getName());
                tripRecord.updateTrip(command.getName(), command);
                if (mIsFaultCodeRead) {
                    try {
                        TroubleCodesCommand troubleCodesCommand = new TroubleCodesCommand();
                        troubleCodesCommand.run(mSocket.getInputStream(), mSocket.getOutputStream());
                        tripRecord.updateTrip(troubleCodesCommand.getName(), troubleCodesCommand);
                        mIsFaultCodeRead = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (mIntent == null)
                    mIntent = new Intent(ACTION_READ_OBD_REAL_TIME_DATA);
                sendBroadcast(mIntent);

            } catch (Exception e) {
                L.i("execute command Exception  :: " + e.getMessage());

                if (!TextUtils.isEmpty(e.getMessage()) && (e.getMessage().equals("Broken pipe") || e.getMessage().equals("Connection reset by peer"))) {
                    L.i("command Exception  :: " + e.getMessage());
                    setDisconnection();
/*
                    if (mLastNotificationType != OBD_NOT_RESPONDING) {
                        mLastNotificationType = OBD_NOT_RESPONDING;
                        updateNotification(getString(R.string.obd2_adapter_not_responding));
                    }
*/
                }
            }
            count++;
            if (count == commands.size()) {
                count = 0;
            }

        }

        // exit loop means connection lost, so set connection status false
        isConnected = false;

    }

    /**
     * send broadcast with specific action and data
     *
     * @param action
     * @param data
     */
    private void sendBroadcast(final String action, String data) {
        final Intent intent = new Intent(action);
        intent.putExtra(ObdReaderService.INTENT_OBD_EXTRA_DATA, data);
        sendBroadcast(intent);
    }

    /**
     * send broadcast with specific action
     *
     * @param action
     */
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //fetchLocation();
        ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(true);
        ObdPreferences.get(getApplicationContext()).setIsOBDconnected(false);
        //   mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        L.i("Service Created :: ");
    }


    /**
     * check whether this devices support bluetooth
     *
     * @return
     */
    protected boolean initiateConnection() {
        boolean isBlueToothSupported = getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);
        boolean isInitialized = initialize();

        if (!isBlueToothSupported || !isInitialized) {
            Toast.makeText(this, getString(R.string.bluetooth_unsupported), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * check BluetoothServices available in this device or not
     *
     * @return
     */
    public boolean initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {

                return false;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBluetoothAdapter = mBluetoothManager.getAdapter();
        } else {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (mBluetoothAdapter == null) {
            return false;
        }
        return true;
    }

    // display foreground service notification.
/*
    private void setUpAsForeground() {

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, NotificationDummyActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
                ), 0);


        mNotificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.waiting_for_obd))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent);

        startForeground(NOTIFICATION_ID, mNotificationBuilder.build());
    }
*/

   /* *//**
     * Updates the notification.
     *//*
    private void updateNotification(String text) {
        mNotificationBuilder.setContentText(text);
        mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
    }
*/

    /**
     * Updates the notification.
     */
/*
    public void updateNotificationString() {
        String text = "";
        if (mLastNotificationType == OBD_CONNECTED) {
            text = getString(R.string.connected_ok);
        } else if (mLastNotificationType == OBD_NOT_RESPONDING) {
            text = getString(R.string.obd2_adapter_not_responding);
        }
        if (mLastNotificationType == DEVICE_NOT_PAIRED) {
            text = getString(R.string.waiting_for_obd);
        }
        mNotificationBuilder.setContentTitle(getString(R.string.app_name));
        mNotificationBuilder.setContentText(text);
        mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
    }
*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i("service onDestroy");
        //mNotificationManager.cancel(NOTIFICATION_ID);
        closeSocket();
        ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(false);
        ObdPreferences.get(getApplicationContext()).setIsOBDconnected(false);
        TripRecord.getTripRecode(this).clear();
    }

    /**
     * close Bluetooth Socket
     */
    private void closeSocket() {
        L.i("socket closed :: ");
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                L.i("socket closing failed :: ");
            }
        }
    }

    /**
     * check whether Bluetooth is enable or not
     *
     * @return
     */
    public boolean isEnable() {
        if (mBluetoothAdapter == null)
            return false;
        return mBluetoothAdapter.isEnabled();

    }

    /**
     * enable bluetooth without user interaction
     *
     * @return
     */
    public boolean enableBlutooth() {
        if (mBluetoothAdapter != null)
            return mBluetoothAdapter.enable();
        return false;
    }

    @Override
    public boolean stopService(Intent name) {
        ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(false);
        return super.stopService(name);

    }

    /*Method used to set device disconnected state through the application...*/
    public void setDisconnection() {
/*
        if (mLastNotificationType != OBD_NOT_RESPONDING) {
            mLastNotificationType = OBD_NOT_RESPONDING;
            updateNotification(getString(R.string.obd2_adapter_not_responding));
        }
*/

        ObdPreferences.get(getApplicationContext()).setIsOBDconnected(false);
        isConnected = false;
        closeSocket();
        L.i("socket disconnected :: ");
      //  broadcastUpdate(ACTION_OBD_DISCONNECTED);
        sendBroadcast(ACTION_OBD_CONNECTION_STATUS, getString(R.string.connect_lost));
    }

    /*Method used to set device connected state through the application...*/
    private void setConnection(boolean isFromBle) {

        ObdPreferences.get(getApplicationContext()).setIsOBDconnected(true);
        isConnected = true;
       // sendBroadcast(ACTION_OBD_CONNECTED, String.valueOf(isFromBle));
        sendBroadcast(ACTION_OBD_CONNECTION_STATUS, getString(R.string.obd_connected));
    }

    /**
     * create Binder instance used to return in onBind method
     */
    public class LocalBinder extends Binder {
        public ObdReaderService getService() {
            return ObdReaderService.this;
        }
    }


}
