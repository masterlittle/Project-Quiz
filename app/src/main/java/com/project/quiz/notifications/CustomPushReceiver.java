package com.project.quiz.notifications;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;
import com.project.quiz.activities.ActivityEvents;
import com.project.quiz.utils.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shitij on 26/10/15.
 */
public class CustomPushReceiver extends ParsePushBroadcastReceiver {

    private final String TAG = CustomPushReceiver.class.getSimpleName();

    private Intent parseIntent;
    private NotificationUtils notificationUtils;

    public CustomPushReceiver() {
        super();
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        if(intent == null)
            return;
        else{
            try {
                JSONObject object = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                Log.i(TAG, "Push received" + object);
                parseIntent  = intent;
                parsePushJson(context,object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parsePushJson(Context context, JSONObject object) {
        try {
            String title = object.getString("title");
            String message = object.getString("message");
            Intent resultIntent = new Intent(context, ActivityEvents.class);
            showNotificationMessage(context, title, message, resultIntent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showNotificationMessage(Context context, String title, String message, Intent resultIntent) {
        notificationUtils = new NotificationUtils(context);
        resultIntent.putExtras(parseIntent.getExtras());
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, resultIntent);
    }
}
