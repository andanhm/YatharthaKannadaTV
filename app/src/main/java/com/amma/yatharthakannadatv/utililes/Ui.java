package com.amma.yatharthakannadatv.utililes;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amma.yatharthakannadatv.R;

public class Ui {
    /**
     * Utility class
     */


    private Context mContext = null;

    /**
     * Public constructor that takes mContext for later use.Provides information about an application environment.
     */
    public Ui(@NonNull Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Make a standard toast that just contains a text view.
     *
     * @param text The text to show.  Can be formatted text.
     */
    public void toast(@NonNull String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
    }

    /**
     * Make a standard toast that just contains a text view.
     *
     * @param resId The new text for the Toast.
     */
    public void toast(@StringRes int resId) {
        Toast.makeText(mContext, resId, Toast.LENGTH_LONG).show();
    }

    /**
     * Make a Snackbar to display a message
     *
     * @param text The text to show.  Can be formatted text.
     */
    public void snackBar(@NonNull String text) {
        Snackbar.make(((Activity) mContext).findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Make a Snackbar to display a message
     *
     * @param resId The new text for the Snackbar.
     */
    public void snackBar(@StringRes int resId) {
        Snackbar.make(((Activity) mContext).findViewById(android.R.id.content), resId, Snackbar.LENGTH_LONG).show();
    }


    /**
     * AlertDialog used to display if any intent provided not available
     */
    void noAppFound(@NonNull final Activity activity) {
        try {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_app_not_found_layout, null);
            dialogBuilder.setView(dialogView);

            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            ImageView close_dialog = (ImageView) dialogView.findViewById(R.id.close_dialog);
            close_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        } catch (Exception e) {
            Toast.makeText(activity, R.string.no_app_found, Toast.LENGTH_SHORT).show();
        }
    }
}
