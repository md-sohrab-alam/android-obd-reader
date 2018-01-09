package com.sohrab.obd.reader.constants;

/**
 * Created by EMP203 on 5/17/2017.
 * <p>
 * provides constant used in Application
 */

public interface DefineObdReader {

    String ACTION_CONNECTION_STATUS_MSG = "ACTION_CONNECTION_STATUS_MSG";
    String ACTION_OBD_MAF_STATUS = "ACTION_OBD_MAF_STATUS";
    /**
     * Real-time data
     */
    String ACTION_READ_OBD_REAL_TIME_DATA = "com.sohrab.obd.reader.ACTION_READ_OBD_REAL_TIME_DATA";

    String ACTION_OBD_CONNECTED = "com.sohrab.obd.reader.ACTION_OBD_CONNECTED";
    // receive when OBD-2 disconnected
    String ACTION_OBD_DISCONNECTED = "com.sohrab.obd.reader.ACTION_OBD_DISCONNECTED";
    // intent key used to send data
     String INTENT_EXTRA_DATA = "com.sohrab.obd.reader.INTENT_EXTRA_DATA";

}
