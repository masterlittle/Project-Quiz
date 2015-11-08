package com.project.quiz.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Shitij on 29/10/15.
 */
public class EventsAll {

    public static final String EVENTS_ALL_TABLE = "events_all";
    public static final String EVENT = "event";
    public static final String EVENT_ID = "event_id";
    public static final String PARSE_ID = "parse_id";
    public static final String LOCATION = "location";
    public static final String LOCATION_ID = "location_id";
    public static final String DESCRIPTION = "description";
    public static final String START = "start";
    public static final String END = "end";
    public static final String ID = "_id";
    public static final String START_DAY = "start_day";
    public static final String END_DAY = "end_day";
    public static final String COLOR = "color";

    private static final String CREATE_TABLE = "CREATE TABLE "
            + EVENTS_ALL_TABLE
            + "("
            + ID + " integer primary key autoincrement, "
            + EVENT + " TEXT, "
            + EVENT_ID + " TEXT UNIQUE, "
            + LOCATION + " TEXT, "
            + LOCATION_ID + " TEXT, "
            + DESCRIPTION + " TEXT, "
            + START + " INTEGER, "
            + END + " INTEGER, "
            + START_DAY + " INTEGER, "
            + END_DAY + " INTEGER, "
            + COLOR + " INTEGER"
            + ")";
    public static void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        Log.w("CalendarProvider", "Upgrading database from version " + oldVersion
                + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS "+ EVENTS_ALL_TABLE);
        onCreate(db);
    }

    private static void createTables(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

}
