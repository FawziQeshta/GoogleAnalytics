package com.fawzi.googleanalytics.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Utilities {

    private static final String TAG = "Utilities";

    public static void sendClickedEventToFirebase(Context context, String keyBundle, String keyLoge) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString(keyBundle, keyLoge);
        mFirebaseAnalytics.logEvent(keyLoge, bundle);
    }

    public static void sendScreenTrackToFirebase(Context context, String screenName, String screenClass) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
        mFirebaseAnalytics.setUserId("123456");
    }

}
