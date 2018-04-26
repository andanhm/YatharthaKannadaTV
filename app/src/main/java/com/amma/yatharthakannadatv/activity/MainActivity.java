package com.amma.yatharthakannadatv.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import android.support.design.widget.Snackbar;
import android.widget.ProgressBar;

import com.amma.yatharthakannadatv.R;
import com.amma.yatharthakannadatv.utililes.Share;
import com.amma.yatharthakannadatv.utililes.Ui;
import com.amma.yatharthakannadatv.web.ChromeClientCustomPoster;
import com.amma.yatharthakannadatv.analytics.TrackingApp;

public class MainActivity extends AppCompatActivity {
    Activity mActivity;
    WebView mWebView;
    LinearLayout mLoadingPanel, mWebViewLayout;
    private static String[] PERMISSIONS = {Manifest.permission.CALL_PHONE,
            Manifest.permission.CALL_PRIVILEGED};
    private static final int REQUEST_ACCESS = 101;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = MainActivity.this;
        Toolbar mTopToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);
        mTopToolbar.setTitle(R.string.app_name);
        mWebView = findViewById(R.id.webView);
        mWebViewLayout = findViewById(R.id.webViewLayout);
        mLoadingPanel = findViewById(R.id.loadingPanel);
        LinearLayout buttonEmail = findViewById(R.id.buttonEmail);
        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email();
            }
        });
        LinearLayout buttonPhone = findViewById(R.id.buttonPhone);
        buttonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
        playVideoViaWebView();
    }

    private void call() {
        String phone = getResources().getString(R.string.phone);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mActivity.requestPermissions(PERMISSIONS, REQUEST_ACCESS);
                return;
            }
        }
        TrackingApp.call();
        mActivity.startActivity(intent);

    }

    private void email() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[]{getResources().getString(R.string.email)});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                getResources().getString(R.string.app_name));
        startActivity(Intent.createChooser(
                emailIntent, "Send mail..."));
        TrackingApp.email();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void playVideoViaWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setMediaPlaybackRequiresUserGesture(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mWebViewLayout.setVisibility(View.VISIBLE);
                mLoadingPanel.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                setProgressBarIndeterminateVisibility(true);
                super.onPageFinished(view, url);
                view.loadUrl("javascript:(function() { document.getElementsByTagName('video')[0].play(); })()");
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                mWebView.setVisibility(View.INVISIBLE);
                Snackbar.make(mActivity.findViewById(android.R.id.content), error.getDescription(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mWebView.setVisibility(View.INVISIBLE);
                Snackbar.make(mActivity.findViewById(android.R.id.content), description, Snackbar.LENGTH_LONG).show();
            }
        });
        mWebView.loadUrl("https://app.viloud.tv/player/embed/channel/fe81329ea8ebce7118f7f619823845a3?autoplay=1&volume=1&controls=0&title=0&share=0&random=0");
        mWebView.setWebChromeClient(new ChromeClientCustomPoster());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    call();
                } else {

                    // All required permissions have been granted, display contacts fragment.
                    Snackbar.make(mActivity.findViewById(android.R.id.content), R.string.permission_available_contacts,
                            Snackbar.LENGTH_SHORT)
                            .show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;

        switch (orientation) {

            case Configuration.ORIENTATION_LANDSCAPE:
                Intent mainIntent = new Intent(mActivity, VideoActivity.class);
                mActivity.startActivity(mainIntent);
                break;

            case Configuration.ORIENTATION_PORTRAIT:
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Share share = new Share(mActivity);
        switch (item.getItemId()) {
            case R.id.share:
                share.share();
                return true;
            case R.id.reportABug:
                share.reportBug();
                return true;
            case R.id.rateUs:
                share.rateUs();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
