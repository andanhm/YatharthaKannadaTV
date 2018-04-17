package com.amma.yatharthakannadatv;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import android.app.Activity;
import android.content.pm.PackageManager;

/**
 * Utility class that wraps access to the runtime permissions API in M and provides basic helper
 * methods.
 */
public abstract class PermissionUtil {

    /*
   * Check if version is marshmallow and above.
   * Used in deciding to ask runtime permission
   * */
    public static boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

}