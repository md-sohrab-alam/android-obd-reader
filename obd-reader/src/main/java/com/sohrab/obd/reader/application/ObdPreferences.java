package com.sohrab.obd.reader.application;

import android.content.Context;
import android.content.SharedPreferences;

import com.sohrab.obd.reader.constants.PreferencesConstants;


/**
 * Created by Sohrab on 30/11/2017.
 * This is singleton class to save data as key values paires.
 */

public class ObdPreferences implements PreferencesConstants {

    private static ObdPreferences mInstance;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    /**
     * create instance
     *
     * @param context
     */
    private ObdPreferences(Context context) {
        mPrefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
        mEditor.apply();

    }

    /**
     *
     * @return: instance of ObdPreferences
     */
    public static ObdPreferences get(Context context) {
        if (mInstance == null) {
            init(context);
          //  ObdReaderApplication.getInstance().initPreferences();
        }
        return mInstance;
    }

    /**
     * static method to create instance of ObdPreferences
     * @param context
     * @return instance of ObdPreferences
     */
    public static ObdPreferences init(Context context) {
        if (mInstance == null)
            mInstance = new ObdPreferences(context);
        return mInstance;
    }


    /**
     * Method to get faultCode
     *
     * @return
     */
    public String getFaultCode() {
        return mPrefs.getString(FAULT_CODE, null);
    }

    /**
     * Method to set fault code.
     *
     * @param faultCode
     */
    public void setFaultCode(String faultCode) {
        mEditor.putString(FAULT_CODE, faultCode).commit();
    }




    public boolean getIsOBDconnected() {
        return mPrefs.getBoolean(IS_OBD_CONNECTED, false);
    }

    public void setIsOBDconnected(boolean isConnected) {
        mEditor.putBoolean(IS_OBD_CONNECTED, isConnected).commit();
    }

    public boolean getServiceRunningStatus() {
        return mPrefs.getBoolean(SERVICE_RUNNING_STATUS, false);
    }

    public void setServiceRunningStatus(boolean status) {
        mEditor.putBoolean(SERVICE_RUNNING_STATUS, status).commit();
    }

    /**
     * Method to get Gas price.
     */
    public float getGasPrice() {
        return mPrefs.getFloat(GAS_PRICE, 7.0f);
    }

    /**
     * Method to set Gas price.
     *
     * @param gasPrice
     */
    public void setGasPrice(float gasPrice) {
        mEditor.putFloat(GAS_PRICE, gasPrice).commit();
    }

    public float getFuelType() {
        return mPrefs.getFloat(FUEL_TYPE_VALUE, 0f);
    }

    public void setFuelType(float value) {
        mEditor.putFloat(FUEL_TYPE_VALUE, value).commit();
    }


}
