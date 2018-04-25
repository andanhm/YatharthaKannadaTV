package com.amma.yatharthakannadatv.utililes;

/**
 * According to Google, I must "deactivate any calls to Log methods in the source code"
 * before publishing my Android app.
 * This class is proguard
 */

public class Log {
    public static void v(String tag, String message) {
        android.util.Log.e(tag, message);
    }

    public static void d(String tag, String message) {
        android.util.Log.d(tag, message);
    }

    public static void i(String tag, String message) {
        android.util.Log.i(tag, message);
    }

    public static void w(String tag, String message) {
        android.util.Log.w(tag, message);
    }

    public static void e(String tag, String message) {
        android.util.Log.e(tag, message);
    }

    public static void out(String message) {
        System.out.println(message);
    }

    public static void out(int message) {
        System.out.println(message);
    }

    public static void printStackTrace(Exception e) {
        e.printStackTrace();
    }
}
