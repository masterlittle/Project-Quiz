package com.project.quiz.activities;

import android.app.Application;
import android.graphics.Typeface;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.project.quiz.R;

/**
 * Created by Shitij on 01/10/15.
 */
public class QuizApplication extends Application {
    private static Typeface typefaceIconFont;
    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UA-67733512-1";
    private Tracker mTracker;


    public QuizApplication() {
        super();
    }

    @Override
    public void onCreate() {
        typefaceIconFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/android.ttf");
        super.onCreate();
//        Fabric.with(this, new Crashlytics());
    }

    public static Typeface getTypefaceIconFont() {
        return typefaceIconFont;
    }
}

