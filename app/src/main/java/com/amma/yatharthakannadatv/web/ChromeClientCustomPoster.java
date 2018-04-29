package com.amma.yatharthakannadatv.web;

import android.graphics.Bitmap;
import android.webkit.WebChromeClient;

//Remove Android WebView gray play button
public class ChromeClientCustomPoster extends WebChromeClient {
    @Override
    public Bitmap getDefaultVideoPoster() {
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    }
}
