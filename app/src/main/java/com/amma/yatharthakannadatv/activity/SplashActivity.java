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
        setContentView(R.layout.activity_splash);
        mActivity = SplashActivity.this;
        boolean isInternetAvailable = Internet.isConnectionAvailable(3000);
        if (isInternetAvailable) {
            next();
        } else {
            Snackbar.make(SplashActivity.this.findViewById(android.R.id.content), "Check internet connection", Snackbar.LENGTH_LONG).show();
            Snackbar.make(mActivity.findViewById(android.R.id.content), "Check internet connection", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            next();
                        }
                    })
                    .show();
        }

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

        /* New Handler to start the Menu-Activity and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, 2000);

    }
}
