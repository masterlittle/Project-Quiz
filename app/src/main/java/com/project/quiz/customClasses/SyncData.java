package com.project.quiz.customclasses;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.project.quiz.contentprovider.DataContentProvider;
import com.project.quiz.database.StudentRecords;
import com.project.quiz.extendedcalendarview.CalendarProvider;

import java.util.List;

/**
 * Created by Shitij on 29/10/15.
 */
public class SyncData implements LoaderManager.LoaderCallbacks<Cursor> {

    private static Context context;

    public void sync(Context mContext, Activity mActivity) {
        context = mContext;
        syncStudentsToServer(context, mActivity);
    }

    private static void syncEventsFromServer() {
        emptyTable();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuizEvents");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> eventList, ParseException e) {
                if (e == null) {
                    if (eventList.size() > 0) {
                        ContentValues values[] = new ContentValues[eventList.size()];
                        int i = 0;
                        for (ParseObject event : eventList) {
                            ContentValues value = new ContentValues();
                            value.put(CalendarProvider.EVENT, event.get(CalendarProvider.EVENT).toString());
                            value.put(CalendarProvider.DESCRIPTION, event.get(CalendarProvider.DESCRIPTION).toString());
                            value.put(CalendarProvider.START, event.get(CalendarProvider.START).toString());
                            value.put(CalendarProvider.END, event.get(CalendarProvider.END).toString());
                            value.put(CalendarProvider.START_DAY, event.get(CalendarProvider.START_DAY).toString());
                            value.put(CalendarProvider.END_DAY, event.get(CalendarProvider.END_DAY).toString());
                            value.put(CalendarProvider.LOCATION, event.get(CalendarProvider.LOCATION).toString());
                            value.put(CalendarProvider.EVENT_ID, event.get(CalendarProvider.EVENT_ID).toString());
                            value.put(CalendarProvider.COLOR, Integer.parseInt(event.get(CalendarProvider.COLOR).toString()));
                            value.put(CalendarProvider.LOCATION_ID, event.get(CalendarProvider.LOCATION_ID).toString());
                            value.put(CalendarProvider.COLOR, event.get(CalendarProvider.COLOR).toString());
                            values[i] = value;
                            i++;
                        }
                        context.getContentResolver().bulkInsert(CalendarProvider.CONTENT_BULK_INSERT_URI, values);
                    }
                } else {
                    Log.d("Update events", "Error: " + e.getMessage());
                }
            }
        });
    }

    private static void emptyTable() {
        context.getContentResolver().delete(CalendarProvider.CONTENT_ALL_EVENT_URI, null, null);
    }

    private static void syncStudentsFromServer() {
        emptyStudents();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Students");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> studentsList, ParseException e) {
                if (e == null) {
                    if (studentsList.size() > 0) {
                        final ContentValues values[] = new ContentValues[studentsList.size()];
                        int i = 0;
                        for (ParseObject student : studentsList) {
                            ContentValues value = new ContentValues();
                            value.put(StudentRecords.STUDENT_ID, student.get(StudentRecords.STUDENT_ID).toString());
                            value.put(StudentRecords.STUDENT_NAME, student.get(StudentRecords.STUDENT_NAME).toString());
                            value.put(StudentRecords.STUDENT_SCORE, student.get(StudentRecords.STUDENT_SCORE).toString());
                            value.put(StudentRecords.STUDENT_YEAR, student.get(StudentRecords.STUDENT_YEAR).toString());
                            value.put(StudentRecords.STUDENT_SELECTED, student.get(StudentRecords.STUDENT_SELECTED).toString());
                            values[i++] = value;
                        }
                        context.getContentResolver().bulkInsert(DataContentProvider.CONTENT_BULK_INSERT_STUDENTS_URI, values);
                    }
                } else {
                    Log.d("Update events", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void syncStudentsToServer(Context context, Activity activity) {
        activity.getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String projection[] = new String[]{StudentRecords.COLUMN_ID, StudentRecords.STUDENT_NAME, StudentRecords.STUDENT_SCORE, StudentRecords.STUDENT_SELECTED, StudentRecords.STUDENT_ID, StudentRecords.STUDENT_YEAR};
        return new CursorLoader(context, DataContentProvider.CONTENT_STORE_STUDENTS_URI, projection, null, null, StudentRecords.STUDENT_NAME + " asc");

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
        if(loader.getId() == 0) {
            ParseQuery<ParseObject> query = new ParseQuery<>("Students");
            query.whereExists(StudentRecords.STUDENT_SCORE);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        if (objects.size() > 0 && cursor.getCount() > 0) {
                            while (cursor.moveToNext()) {
                                for (ParseObject student : objects) {
                                    if (cursor.getString(cursor.getColumnIndex(StudentRecords.STUDENT_ID)).equalsIgnoreCase(student.getString(StudentRecords.STUDENT_ID))) {
                                        student.put(StudentRecords.STUDENT_SCORE, cursor.getString(cursor.getColumnIndex(StudentRecords.STUDENT_SCORE)));
                                        student.saveEventually();
                                    }
                                }
                            }
                        }
                        syncEventsFromServer();
                        syncStudentsFromServer();
                    }
                }
            });
        }
    }

    private static void emptyStudents() {
        context.getContentResolver().delete(DataContentProvider.CONTENT_EMPTY_STUDENTS_URI, null, null);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
