package com.amma.yatharthakannadatv.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.amma.yatharthakannadatv.R;
import com.amma.yatharthakannadatv.utililes.Internet;

public class SplashActivity extends AppCompatActivity {
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        getWindow().getDecorView().setSystemUiVisibility(3328);
        setContentView(R.layout.activity_sample);
        mActivity = SplashActivity.this;
//        next();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void next() {
        boolean isInternetAvailable = Internet.isConnectionAvailable(5000);
        if (isInternetAvailable) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
            }, 2000);
        } else {
            Snackbar.make(mActivity.findViewById(android.R.id.content), "Check internet connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            next();
                        }
                    })
                    .show();
        }

        /* New Handler to start the Menu-Activity and close this Splash-Screen after some seconds.*/

    }
}
