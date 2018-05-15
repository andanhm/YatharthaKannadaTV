package com.amma.yatharthakannadatv.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.amma.yatharthakannadatv.R;
import com.amma.yatharthakannadatv.web.ChromeClientCustomPoster;

public class VideoActivity extends AppCompatActivity {
    WebView mWebView;
    Activity mActivity;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        getWindow().getDecorView().setSystemUiVisibility(3328);
        setContentView(R.layout.activity_video);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                playVideoViaWebView();
                mActivity = VideoActivity.this;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void playVideoViaWebView(){
        mWebView=findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setMediaPlaybackRequiresUserGesture(true);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                setProgressBarIndeterminateVisibility(true);
                super.onPageFinished(view, url);
                view.loadUrl("javascript:(function() { document.getElementsByTagName('video')[0].play(); })()");
            }
        });
        mWebView.loadUrl("https://app.viloud.tv/player/embed/channel/8e9b2b2234b5d86e62ac20c2eec5e6cf?autoplay=1&volume=1&controls=0&title=0&share=0&random=0");
//        mWebView.loadUrl("file:///android_asset/index.html");   // now it will not fail here
        mWebView.setWebChromeClient(new ChromeClientCustomPoster());
        mWebView.setVisibility(View.VISIBLE);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation=newConfig.orientation;

        switch(orientation) {

            case Configuration.ORIENTATION_LANDSCAPE:
                break;

            case Configuration.ORIENTATION_PORTRAIT:
                Intent mainIntent = new Intent(mActivity, MainActivity.class);
                mActivity.startActivity(mainIntent);
                mActivity.finish();
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.loadUrl("javascript:(function() { document.getElementsByTagName('video')[0].play(); })()");
    }
}
