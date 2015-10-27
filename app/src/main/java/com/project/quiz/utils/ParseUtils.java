package com.project.quiz.utils;

import android.content.Context;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

/**
 * Created by Shitij on 26/10/15.
 */
public class ParseUtils  {
    private static String TAG = ParseUtils.class.getSimpleName();

    public static void registerParse(Context context){
        Parse.initialize(context, Api.AppConfig.APP_ID, Api.AppConfig.CLIENT_ID);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground(Api.AppConfig.PARSE_CHANNEL, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Log.e(TAG, "Successfully subscribed");
                }
            }
        });
    }

}
