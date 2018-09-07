package com.amma.yatharthakannadatv.analytics;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

public class TrackingApp {

    //We can config in your app to show only for your particular project
    public static void call() {
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Call")
                .putContentType("Button"));
    }
    public static void email() {
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Email")
                .putContentType("Button"));
    }
    public static void share() {
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Share")
                .putContentType("Menu"));
    }
    public static void rate() {
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Rate")
                .putContentType("Menu"));
    }
}
