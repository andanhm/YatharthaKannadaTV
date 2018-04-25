package com.amma.yatharthakannadatv.web;

import android.graphics.Bitmap;
import android.webkit.WebChromeClient;

//Remove Android WebView gray play button
public class ChromeClientCustomPoster extends WebChromeClient {
    @Override
    public Bitmap getDefaultVideoPoster() {
        return Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
    }
}
