package com.amma.yatharthakannadatv.permission;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
    private static final String PREFERENCE_NAME="com.amma.yatharthakannadatv.permission";

    public static final String CALL_PHONE_REQUEST_CODE="CALL_PHONE_REQUEST";

    public static void setPreference(Context mContext, String KEY) {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY, true);
        editor.apply();
    }

    public static boolean getPreference(Context mContext, String KEY) {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY, false);
    }
}
