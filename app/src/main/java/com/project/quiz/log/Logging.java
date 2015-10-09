/*
 * Copyright 2015 Amandeep Anguralla
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.project.quiz.log;

import android.util.Log;

import com.project.quiz.BuildConfig;
import com.project.quiz.utils.CommonLibs;

import java.util.Map;

/**
 * Created by Amandeep Anguralla on 3/13/15.
 */
public class Logging {

    public static final String REQUEST_TYPE = "REQUEST_TYPE";
    public static final String REQUEST_URL = "REQUEST_URL";
    public static final String REQUEST_BODY = "REQUEST_BODY";

    public static final String TYPE_GET = "GET";
    public static final String TYPE_POST = "POST";
    public static final String TYPE_PUT = "PUT";
    public static final String TYPE_DELETE = "DELETE";

    /**
     * Function used to log exception
     * @param LOG_TAG is the tag for message
     * @param exception is the exception to be logged
     */
    public static void logException(String LOG_TAG, Exception exception, @CommonLibs.Priority.PriorityConstants int priority)
    {
        switch (priority) {
            case CommonLibs.Priority.VERY_HIGH:

            case CommonLibs.Priority.HIGH:
                if (exception.getMessage() != null) {
                    if (BuildConfig.DEBUG)
                        Log.e(LOG_TAG, exception.getMessage(), exception);

                    else {
//                        Crashlytics.getInstance().core.logException(exception);
//                        Sentry.captureException(exception);
                    }
                }
                break;

            case CommonLibs.Priority.MEDIUM:

            case CommonLibs.Priority.LOW:

            case CommonLibs.Priority.VERY_LOW:
                if(BuildConfig.DEBUG)
                    Log.e(LOG_TAG, exception.getMessage(), exception);
                break;

            default:
                if(BuildConfig.DEBUG)
                    Log.e(LOG_TAG, exception.getMessage(), exception);
        }
    }

    /**
     * Method to log Errors from application
     *
     * @param LOG_TAG is the tag for message
     * @param error is the error message to be logged

     */
    public static void logError(String LOG_TAG, String error, @CommonLibs.Priority.PriorityConstants int priority)
    {
        switch (priority) {
            case CommonLibs.Priority.VERY_HIGH:
                if(error != null && error.trim().length() > 0) {

                    if(BuildConfig.DEBUG)
                        Log.e(LOG_TAG, error);

                    else {
//                        Sentry.captureMessage(error, Sentry.SentryEventBuilder.SentryEventLevel.FATAL);
//                        Crashlytics.getInstance().core.log(Log.ERROR, LOG_TAG, error);
                    }

                }
                break;

            case CommonLibs.Priority.HIGH:
                if(error != null && error.trim().length() > 0) {
                    if(BuildConfig.DEBUG)
                        Log.e(LOG_TAG, error);

                    else {
//                        Crashlytics.getInstance().core.log(Log.ERROR, LOG_TAG, error);
//                        Sentry.captureMessage(error, Sentry.SentryEventBuilder.SentryEventLevel.ERROR);
                    }
                }
                break;

            case CommonLibs.Priority.MEDIUM:

            case CommonLibs.Priority.LOW:

            case CommonLibs.Priority.VERY_LOW:
                if(BuildConfig.DEBUG)
                    Log.e(LOG_TAG, error);
                break;

            default:
                if(BuildConfig.DEBUG)
                    Log.e(LOG_TAG, error);
        }
    }

    /**
     * Method to log messages from applications
     *
     * @param LOG_TAG is the tag for message
     * @param message is the message to be logged
     */
    public static void logMessage(String LOG_TAG, String message, @CommonLibs.Priority.PriorityConstants int priority)
    {
        switch (priority) {
            case CommonLibs.Priority.VERY_HIGH:
                if(message != null && message.trim().length() > 0) {

                    if(BuildConfig.DEBUG)
                        Log.d(LOG_TAG, message);

                    else {
//                        Crashlytics.getInstance().core.log(Log.ERROR, LOG_TAG, message);
//                        Sentry.captureMessage(message.trim(), Sentry.SentryEventBuilder.SentryEventLevel.DEBUG);
                    }
                }
                break;

            case CommonLibs.Priority.HIGH:
                if(message != null && message.trim().length() > 0) {
                    if(BuildConfig.DEBUG)
                        Log.d(LOG_TAG, message);

                    else {
//                        Sentry.captureMessage(message.trim(), Sentry.SentryEventBuilder.SentryEventLevel.INFO);
//                        Crashlytics.getInstance().core.log(Log.ERROR, LOG_TAG, message);
                    }
                }
                break;

            case CommonLibs.Priority.MEDIUM:

            case CommonLibs.Priority.LOW:

            case CommonLibs.Priority.VERY_LOW:
                if(BuildConfig.DEBUG)
                    Log.d(LOG_TAG, message);
                break;

            default:
                if(BuildConfig.DEBUG)
                    Log.d(LOG_TAG, message);
        }
    }

    /**
     * Method to log Errors from application
     *
     * @param LOG_TAG is the tag for message
     * @param error is the error message to be logged
     * @param priority is the priority of the logging defined by {@link CommonLibs.Priority class}
     * @param dict is the extra params to be passed with exception
     *
     */
    public static void logError(String LOG_TAG, String error, @CommonLibs.Priority.PriorityConstants int priority, Map<String, String> dict)
    {
        switch (priority) {
            case CommonLibs.Priority.VERY_HIGH:
                if(error != null && error.trim().length() > 0) {
                    if(BuildConfig.DEBUG)
                        Log.e(LOG_TAG, error + "\nDATA :\n" + dict.toString());

                    else {
//                        Crashlytics.getInstance().core.log(Log.ERROR, LOG_TAG, error);
//                        Sentry.SentryEventBuilder builder = new Sentry.SentryEventBuilder();
//                        builder.setLevel(Sentry.SentryEventBuilder.SentryEventLevel.FATAL);
//                        builder.setMessage(error);
//                        builder.setExtra(dict);
//                        Sentry.captureEvent(builder);
                    }
                }
                break;

            case CommonLibs.Priority.HIGH:
                if(error != null && error.trim().length() > 0) {
                    if(BuildConfig.DEBUG)
                        Log.e(LOG_TAG, error + "\nDATA :\n" + dict.toString());

                    else {
//                        Crashlytics.getInstance().core.log(Log.ERROR, LOG_TAG, error);
//                        Sentry.SentryEventBuilder builder = new Sentry.SentryEventBuilder();
//                        builder.setLevel(Sentry.SentryEventBuilder.SentryEventLevel.FATAL);
//                        builder.setMessage(error);
//                        builder.setExtra(dict);
//                        Sentry.captureEvent(builder);
                    }
                }
                break;

            case CommonLibs.Priority.MEDIUM:

            case CommonLibs.Priority.LOW:

            case CommonLibs.Priority.VERY_LOW:
                if(BuildConfig.DEBUG)
                    Log.e(LOG_TAG, error + "\nDATA :\n" + dict.toString());
                break;

            default:
                if(BuildConfig.DEBUG)
                    Log.e(LOG_TAG, error + "\nDATA :\n" + dict.toString());
        }
    }
}
