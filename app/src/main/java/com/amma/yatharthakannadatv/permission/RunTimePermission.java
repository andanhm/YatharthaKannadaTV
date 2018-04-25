package com.amma.yatharthakannadatv.permission;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;

import com.amma.yatharthakannadatv.R;

public class RunTimePermission {
    public static final int CALL_PHONE_REQUEST_CODE = 105;

    /**
     * Method allows get the sdk version is M /or
     */
    public static boolean areExplicitPermissionsRequired() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    /**
     * Method check call permission for M if not permission will be requested
     *
     * @param activity Activity context
     */
    public static void getCallPermission(Activity activity) {
        try {
            if (areExplicitPermissionsRequired()) {
                int hasPermission = activity.checkSelfPermission(Manifest.permission.CALL_PHONE);
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    activity.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_REQUEST_CODE);
                }
            }

        } catch (Exception e) {
//            ApplicationTracker.getInstance().trackException(e);
        }
    }

    /**
     * Method check whether user granted permission or not
     *
     * @param grantResults permission set granted by user
     */
    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method check permission granted
     *
     * @param activity   Activity context
     * @param permission permission that need to be checked
     */
    public static boolean isPermissionGranted(Activity activity, String permission) {
        return PermissionChecker.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Method allows to app permission intent
     *
     * @param activity Activity context
     */
    public static void appSettings(Activity activity) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    /**
     * Method open pop up for requesting for permission again
     *
     * @param activity      Activity context
     * @param string_res_id Message to be displayed
     */
    public static void show(final Activity activity, int string_res_id) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.alert_permission)
                    .setCancelable(false)
                    .setMessage(string_res_id)
                    .setIcon(R.drawable.warning)
                    .setNegativeButton(R.string.alert_permission_open_setting,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    appSettings(activity);
                                    dialog.dismiss();
                                }
                            }).show();
        } catch (Exception e) {
//            ApplicationTracker.getInstance().trackException(e);
        }
    }

}
