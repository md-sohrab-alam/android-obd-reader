## android-obd-reader
Android OBD-II Reader library that support standard PIDs (Mode 01). 

![screenshot](/PidsValuesScreenShort.png)

## Motivation
I was working for an application that read Car real time data through OBD-II. For connectivity of Car Head Unit with OBD-II and read real time data, we spend lots of time because of no proper documentation and connectivity guide. Therefore, I developed this library so that if anyone looking for the same, they can use and save time.

## Feature
Check which PIDs supported in your vehicle if you do not set any command.
Based on standard PIDs, we are calculating followings:

1: Distance Traveled 
2: Idle Time
3: Driving Time
4: Max Speed in one Trip
5: Max RPM 
6: Idling Fuel Consumtion
7: Driving Fuel Consumtion
8: Instant Fuel Consumtion
9: Rapid Acceleration Times (How many times speed increases very fast)
10:Rapid Declaration Times (How many time emergency brake is used)

## Source Enviourment

* Android Studio : 3.0.1
* Gradle : 3.0.1

## Project Configuration

* minSdkVersion 15
* targetSdkVersion 26
* compileSdkVersion 26

## Getting Started
Add the following to your build.gradle to use:  

dependencies {
    compile 'com.sohrab:obd-reader:1.0.0'
}

# Usage

   * Follow sample-app:   
   
   ArrayList<ObdCommand> obdCommands = new ArrayList<>();
        obdCommands.add(new SpeedCommand());
        obdCommands.add(new RPMCommand());
        ObdConfiguration.setmObdCommands(this, obdCommands);

If you want to read all standard PIDs commands, pass null in the second argument like        
      //  ObdConfiguration.setmObdCommands(this, null);

Set gas price per liter so that gas cost can be calculated accordingly. Default is 7 $/l

float gasPrice = 7; // per litre, you should initialize according to your requirement.
ObdPreferences.get(this).setGasPrice(gasPrice);

Register receiver with some action related to OBD connection status and read PID values

IntentFilter intentFilter = new IntentFilter();
intentFilter.addAction(ACTION_READ_OBD_REAL_TIME_DATA);
intentFilter.addAction(ACTION_OBD_CONNECTION_STATUS);
registerReceiver(mObdReaderReceiver, intentFilter);

start service that keep running in background for connecting and execute command until you stop

startService(new Intent(this, ObdReaderService.class));

Broadcast Receiver to receive OBD connection status and real time data

BroadcastReceiver mObdReaderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {            
	 String action = intent.getAction();
            if (action.equals(ACTION_OBD_CONNECTION_STATUS)) {
                String connectionStatusMsg = intent.getStringExtra(ObdReaderService.INTENT_OBD_EXTRA_DATA);                                             if (connectionStatusMsg.equals(getString(R.string.obd_connected))) {
                    //OBD connected  do what want after OBD connection
                } else if (connectionStatusMsg.equals(getString(R.string.connect_lost))) {
                    //OBD disconnected  do what want after OBD disconnection
                } else {
                    // here you could check OBD connection and pairing status
                }
            } else if (action.equals(ACTION_READ_OBD_REAL_TIME_DATA)) {
                TripRecord tripRecord = TripRecord.getTripRecode(SampleActivity.this);                
                // here you can fetch real time data from TripRecord using getter methods like
                //tripRecord.getSpeed();
                //tripRecord.getEngineRpm();
            }
        }
    };	
	
## Tested

* Head Unit (Generally used in Cars)
* Mobile 
  
   
 

   






   
