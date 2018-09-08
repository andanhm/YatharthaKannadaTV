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
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.amma.yatharthakannadatv.R;
import com.amma.yatharthakannadatv.analytics.TrackingApp;
import com.amma.yatharthakannadatv.utililes.Internet;
import com.amma.yatharthakannadatv.utililes.Share;
import com.amma.yatharthakannadatv.web.ChromeClientCustomPoster;
import com.crashlytics.android.Crashlytics;

public class MainFullActivity extends AppCompatActivity {
    String url = "http://app.viloud.tv/player/embed/channel/8e9b2b2234b5d86e62ac20c2eec5e6cf?autoplay=1&volume=1&controls=0&title=0&share=0&random=0";
    private static final int REQUEST_ACCESS = 101;
    private static String[] PERMISSIONS = {Manifest.permission.CALL_PHONE,
            Manifest.permission.CALL_PRIVILEGED};
    Activity mActivity;
    WebView mWebView;
    LinearLayout mLoadingPanel, mWebViewLayout, mMainLayout;
    Toolbar mTopToolbar;
    View mDecorView;
    private LinearLayout.LayoutParams paramsNotFullscreen;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDecorView = getWindow().getDecorView();

//        Crashlytics.getInstance().crash();
        setContentView(R.layout.activity_main_full);
        mActivity = MainFullActivity.this;
        mTopToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);
        mTopToolbar.setTitle(R.string.app_name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTopToolbar.setTitleTextColor(getResources().getColor(android.R.color.white, null));
        } else {
            mTopToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        }
        mMainLayout = findViewById(R.id.mainLayout);
        mWebView = findViewById(R.id.webView);
        mWebViewLayout = findViewById(R.id.webViewLayout);
        mWebViewLayout.setVisibility(View.GONE);
        mLoadingPanel = findViewById(R.id.loadingPanel);
        mLoadingPanel.setVisibility(View.VISIBLE);
        playVideoViaWebView();
        boolean isInternetAvailable = Internet.isConnectionAvailable(5000);
        if (!isInternetAvailable) {
            Snackbar.make(mActivity.findViewById(android.R.id.content), getResources().getString(R.string.check_internet), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
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

    @Override
    protected void onResume() {
        super.onResume();
//        mWebView.loadUrl("javascript:(function() { document.getElementsByTagName('video')[0].play(); })()");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mWebView.loadUrl("javascript:(function() { document.getElementsByTagName('video')[0].stop(); })()");
    }

    private void email() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[]{getResources().getString(R.string.email)});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                getResources().getString(R.string.app_name));
        startActivity(Intent.createChooser(
                emailIntent, getResources().getString(R.string.chooser_email_intent)));
        TrackingApp.email();
    }

    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    private void playVideoViaWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setMediaPlaybackRequiresUserGesture(true);
        mWebView.setWebChromeClient(new ChromeClientCustomPoster());
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
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mWebViewLayout.setVisibility(View.VISIBLE);
                        mLoadingPanel.setVisibility(View.GONE);
                    }
                }, 2000);
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                mWebView.setVisibility(View.GONE);
                mLoadingPanel.setVisibility(View.VISIBLE);
                Snackbar.make(mActivity.findViewById(android.R.id.content), error.getDescription().toString(), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mActivity.recreate();
                            }
                        })
                        .show();
                Crashlytics.log(error.getDescription().toString());
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mWebView.setVisibility(View.GONE);
                mLoadingPanel.setVisibility(View.VISIBLE);
                Snackbar.make(mActivity.findViewById(android.R.id.content), description, Snackbar.LENGTH_INDEFINITE)
                        .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mActivity.recreate();
                            }
                        })
                        .show();
                Crashlytics.log(description);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url); // you are using siteView here instead of view
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        mWebView.invalidate();
        mWebView.setInitialScale(100);
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return false;
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                    mDecorView.setSystemUiVisibility(uiOptions);
                }
                return false;
            }
        });
//        mWebView.loadUrl(url);
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
                TrackingApp.share();
                return true;
            case R.id.reportABug:
                share.reportBug();
                return true;
            case R.id.rateUs:
                share.rateUs();
                TrackingApp.rate();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fullScreen() {
        // Hide Status Bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDecorView.setSystemUiVisibility(uiOptions);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        paramsNotFullscreen = (LinearLayout.LayoutParams) mWebViewLayout.getLayoutParams();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(paramsNotFullscreen);
        params.setMargins(0, 0, 0, 0);
        params.height = LinearLayout.LayoutParams.MATCH_PARENT;
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        mWebViewLayout.setLayoutParams(params);
//        mMainLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
        mTopToolbar.setVisibility(View.GONE);
    }

    public void exitFullScreen() {
        // Show Status Bar.
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        mDecorView.setSystemUiVisibility(uiOptions);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (paramsNotFullscreen == null) {
            paramsNotFullscreen = (LinearLayout.LayoutParams) mWebViewLayout.getLayoutParams();
        }
        mWebViewLayout.setLayoutParams(paramsNotFullscreen);
//        mMainLayout.setBackground(getResources().getDrawable(R.drawable.app_background));
        mTopToolbar.setVisibility(View.VISIBLE);
    }
}
