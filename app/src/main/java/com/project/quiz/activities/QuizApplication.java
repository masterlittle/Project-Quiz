package com.project.quiz.activities;

import android.app.Application;
import android.graphics.Typeface;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.project.quiz.utils.Api;
import com.project.quiz.utils.ParseUtils;

/**
 * Created by Shitij on 01/10/15.
 */
public class QuizApplication extends Application {
    private static Typeface typefaceIconFont;
    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UA-67733512-1";

    public QuizApplication() {
        super();
    }

    @Override
    public void onCreate() {
        typefaceIconFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/android.ttf");
        //Initialize Parse
        Parse.enableLocalDatastore(this);
        ParseUtils.registerParse(this);
        super.onCreate();
//        Fabric.with(this, new Crashlytics());
    }

    public static Typeface getTypefaceIconFont() {
        return typefaceIconFont;
    }
}

