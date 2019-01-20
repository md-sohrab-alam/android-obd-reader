## android-obd-reader

[![Open Source Love](https://badges.frapsoft.com/os/v2/open-source.svg?v=103)](https://github.com/md-sohrab-alam/android-obd-reader) 
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/md-sohrab-alam/android-obd-reader/blob/master/LICENSE)

Android OBD-II Reader library that support standard PIDs (Mode 01) in Mobile and Head Unit (insatalled in Car). 

![screenshot](/PidsValuesScreenShort.png)

## Motivation
I was working for an application that read Car real time data through OBD-II. I found a library https://github.com/pires/obd-java-api , it is working fine in mobile, but there is problem to connect OBD with with Head Unit. So, for connectivity of Car Head Unit with OBD-II and read real time data, we spend lots of time because of no proper documentation and connectivity guide. Therefore, I developed this library so that if anyone looking for the same, they can use and save time.

## Feature
Check which PIDs supported in your vehicle if you do not set any command.
Based on standard PIDs, we are calculating followings:

* Distance Traveled 
* Idle Time
* Driving Time
* Max Speed in one Trip
* Max RPM 
* Idling Fuel Consumtion
* Driving Fuel Consumtion
* Instant Fuel Consumtion
* Rapid Acceleration Times (How many times speed increases very fast)
* Rapid Declaration Times (How many time emergency brake is used)

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
   ``` java
   ArrayList<ObdCommand> obdCommands = new ArrayList<>();
        obdCommands.add(new SpeedCommand());
        obdCommands.add(new RPMCommand());
        ObdConfiguration.setmObdCommands(this, obdCommands);
```

 ``` java
//If you want to read all standard PIDs commands, pass null in the second argument like        
        ObdConfiguration.setmObdCommands(this, null);
```      

``` java
//Set gas price per liter so that gas cost can be calculated accordingly. Default is 7 $/l
float gasPrice = 7; // per litre, you should initialize according to your requirement.
ObdPreferences.get(this).setGasPrice(gasPrice);
```

 ``` java
//Register receiver with some action related to OBD connection status and read PID values
IntentFilter intentFilter = new IntentFilter();
intentFilter.addAction(ACTION_READ_OBD_REAL_TIME_DATA);
intentFilter.addAction(ACTION_OBD_CONNECTION_STATUS);
registerReceiver(mObdReaderReceiver, intentFilter);
```
 
 ``` java
//start service that keep running in background for connecting and execute command until you stop
startService(new Intent(this, ObdReaderService.class));
```

``` java
//Broadcast Receiver to receive OBD connection status and real time data
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
 ```
 
## Tested

* Head Unit (Generally used in Cars)
* Mobile 

## Author

[Sohrab Alam](https://www.linkedin.com/in/sohrab-alam-8105474b)

## Appreciation
Thank you to all. It feels good to see that our work is appreciated.

[![paypal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://paypal.me/iamsohrabalam?locale.x=en_GB)

## License

This project is licensed under the Apache License, Version 2.0 - see the [LICENSE.md](LICENSE.md) file for details

  
