package com.sohrab.obd.reader.utils;

import android.text.TextUtils;
import android.util.Log;


/**
 * Log unified management
 *
 * @author way
 */
public class L {
    public static boolean isDebug = true;// Whether to print a bug, you can in the application onCreate function inside initialization
    private static final String TAG = "TEST";

    // The following four are the default tag functions
    public static void i(String msg) {
        if (isDebug && !TextUtils.isEmpty(msg))
            Log.i(TAG, msg);
        //SuperLog.d(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }

    //The following is the incoming class name print log
    public static void i(Class<?> _class, String msg) {
        if (isDebug)
            Log.i(_class.getName(), msg);
      //  SuperLog.i(TAG, msg);
    }

    public static void d(Class<?> _class, String msg) {
        if (isDebug)
            Log.d(_class.getName(), msg);
    }

    public static void e(Class<?> _class, String msg) {
        if (isDebug)
            Log.e(_class.getName(), msg);
    }

    public static void v(Class<?> _class, String msg) {
        if (isDebug)
            Log.v(_class.getName(), msg);
    }

    // The following is a function of passing in a custom tag
    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
         //   SuperLog.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.v(tag, msg);
    }
}
