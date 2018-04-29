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
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.support.design.widget.Snackbar;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.amma.yatharthakannadatv.R;
import com.amma.yatharthakannadatv.utililes.Internet;
import com.amma.yatharthakannadatv.utililes.Share;
import com.amma.yatharthakannadatv.utililes.Ui;
import com.amma.yatharthakannadatv.web.ChromeClientCustomPoster;
import com.amma.yatharthakannadatv.analytics.TrackingApp;

public class MainActivity extends AppCompatActivity {
    Activity mActivity;
    WebView mWebView;
    LinearLayout mLoadingPanel, mWebViewLayout,mFooterLayout,mHeaderLayout;
    private LinearLayout.LayoutParams paramsNotFullscreen;
    private static String[] PERMISSIONS = {Manifest.permission.CALL_PHONE,
            Manifest.permission.CALL_PRIVILEGED};
    private static final int REQUEST_ACCESS = 101;
    Toolbar mTopToolbar;
    ImageView mHeaderImage;
    View mDecorView;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDecorView = getWindow().getDecorView();
        setContentView(R.layout.activity_main);
        mActivity = MainActivity.this;
        mTopToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);
        mTopToolbar.setTitle(R.string.app_name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTopToolbar.setTitleTextColor(getResources().getColor(android.R.color.white,null));
        }else {
            mTopToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        }
        mWebView = findViewById(R.id.webView);
        mWebViewLayout = findViewById(R.id.webViewLayout);
        mLoadingPanel = findViewById(R.id.loadingPanel);
        mHeaderLayout = findViewById(R.id.headerLayout);
        mHeaderImage = findViewById(R.id.imageHeaderIcon);
        mFooterLayout = findViewById(R.id.footerLayout);
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
                boolean isInternetAvailable = Internet.isConnectionAvailable(5000);
        if (!isInternetAvailable) {
            Snackbar.make(mActivity.findViewById(android.R.id.content), "Check internet connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mActivity.recreate();
                        }
                    })
                    .show();

        }
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
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                setProgressBarIndeterminateVisibility(true);
                super.onPageFinished(view, url);
                view.loadUrl("javascript:(function() { document.getElementsByTagName('video')[0].play(); })()");
                mWebViewLayout.setVisibility(View.VISIBLE);
                mLoadingPanel.setVisibility(View.GONE);
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
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) //To fullscreen
        {
            fullScreen();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            exitFullScreen();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mWebView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mWebView.restoreState(savedInstanceState);
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

    public void fullScreen(){
        // Hide Status Bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDecorView.setSystemUiVisibility(uiOptions);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        paramsNotFullscreen=(LinearLayout.LayoutParams)mWebView.getLayoutParams();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(paramsNotFullscreen);
        params.setMargins(0, 0, 0, 0);
        params.height = LinearLayout.LayoutParams.MATCH_PARENT;
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        mWebView.setLayoutParams(params);
        mTopToolbar.setVisibility(View.GONE);
        mHeaderImage.setVisibility(View.GONE);
        mFooterLayout.setVisibility(View.GONE);
    }

    public  void  exitFullScreen(){
        // Show Status Bar.
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        mDecorView.setSystemUiVisibility(uiOptions);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mWebView.setLayoutParams(paramsNotFullscreen);
        mTopToolbar.setVisibility(View.VISIBLE);
        mHeaderImage.setVisibility(View.VISIBLE);
        mFooterLayout.setVisibility(View.VISIBLE);
    }
}
