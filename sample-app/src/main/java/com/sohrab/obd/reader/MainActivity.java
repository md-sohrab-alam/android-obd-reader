package com.sohrab.obd.reader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sohrab.obd.reader.application.Preferences;
import com.sohrab.obd.reader.obdCommand.ObdConfiguration;
import com.sohrab.obd.reader.service.ObdReaderService;
import com.sohrab.obd.reader.trip.TripRecord;

import static com.sohrab.obd.reader.constants.DefineObdReader.ACTION_CONNECTION_STATUS_MSG;
import static com.sohrab.obd.reader.constants.DefineObdReader.ACTION_OBD_CONNECTED;
import static com.sohrab.obd.reader.constants.DefineObdReader.ACTION_OBD_DISCONNECTED;
import static com.sohrab.obd.reader.constants.DefineObdReader.ACTION_READ_OBD_REAL_TIME_DATA;

/**
 * Created by sohrab on 30/11/2017.
 * Sample activity to display OBD data
 */
public class MainActivity extends AppCompatActivity {

    private TextView mObdInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mObdInfoTextView = findViewById(R.id.tv_obd_info);

        /**
         *  configure obd: add required command in arrayList and set to ObdConfiguration.
         *  If you dont set any command or passing null, then all command OBD command will be requested.
         *  Therefore, it is recommended to set command that is required only like belows commented line.         *
         */


     /*   ArrayList<ObdCommand> obdCommands = new ArrayList<>();
        obdCommands.add(new SpeedCommand());
        obdCommands.add(new RPMCommand());
        ObdConfiguration.setmObdCommands(this, obdCommands);*/

        // passing null means we are executing all OBD command for now, but you should add required command for fast retrieval like above commented lines.
        ObdConfiguration.setmObdCommands(this, null);


        /**
         * Register receiver with some action related to OBD connection status
         */
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_OBD_CONNECTED);
        intentFilter.addAction(ACTION_OBD_DISCONNECTED);
        intentFilter.addAction(ACTION_READ_OBD_REAL_TIME_DATA);
        intentFilter.addAction(ACTION_CONNECTION_STATUS_MSG);
        registerReceiver(mObdReaderReceiver, intentFilter);

        //start service which will execute in background for connecting and execute command until you stop
        startService(new Intent(this, ObdReaderService.class));
    }

    /**
     * Broadcast Receiver to receive OBD connection status and real time data
     */
    private final BroadcastReceiver mObdReaderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
            mObdInfoTextView.setVisibility(View.VISIBLE);
            String action = intent.getAction();
            if (ACTION_OBD_CONNECTED.equals(action)) {
                Toast.makeText(MainActivity.this, getString(R.string.deviceconnectionsuccess), Toast.LENGTH_SHORT).show();
                mObdInfoTextView.setText(getString(R.string.deviceconnectionsuccess));
            } else if (ACTION_OBD_DISCONNECTED.equals(action)) {
                Toast.makeText(MainActivity.this, getString(R.string.connect_lost), Toast.LENGTH_SHORT).show();
                mObdInfoTextView.setText(getString(R.string.connect_lost));
            } else if (action.equals(ACTION_READ_OBD_REAL_TIME_DATA)) {
                TripRecord tripRecord = TripRecord.getTripRecode(MainActivity.this);
                mObdInfoTextView.setText(tripRecord.toString());

                // here you can fetch real time data from TripRecord using getter methods like
                //tripRecord.getSpeed();
                //tripRecord.getEngineRpm();
            } else if (action.equals(ACTION_CONNECTION_STATUS_MSG)) {
                if (Preferences.get(MainActivity.this).getServiceRunningStatus()) {
                    String connectionStatusMsg = intent.getStringExtra(ObdReaderService.INTENT_EXTRA_DATA);
                    mObdInfoTextView.setText(connectionStatusMsg);
                    Toast.makeText(MainActivity.this, connectionStatusMsg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregister receiver
        unregisterReceiver(mObdReaderReceiver);
        //stop service
        stopService(new Intent(this, ObdReaderService.class));
        // This will stop background thread if any running immediately.
        Preferences.get(this).setServiceRunningStatus(false);
    }

}
