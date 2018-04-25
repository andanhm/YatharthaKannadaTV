package com.amma.yatharthakannadatv.utililes;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

public class DeviceUtil {

    public static int getScreenWidth(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static float dpToPx(final float dp) {
        return dp * Resources.getSystem().getDisplayMetrics().density;
    }

}
