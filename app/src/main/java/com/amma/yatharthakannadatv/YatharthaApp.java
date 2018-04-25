package com.amma.yatharthakannadatv;

import android.app.Application;

import com.amma.yatharthakannadatv.utililes.Log;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;

import io.fabric.sdk.android.Fabric;

/**
 * Includes one-time initialization
 * <p>
 * This is a subclass of {@link Application} used to provide shared objects for this app,
 */

public class YatharthaApp extends Application {
    public static final String TAG = YatharthaApp.class.getSimpleName();


    private static YatharthaApp mYatharthaApp;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new Answers());

        mYatharthaApp = this;
        Log.d(TAG, "APP LAUNCHED");
    }

    public static YatharthaApp getInstance() {
        return mYatharthaApp;

    }

}
