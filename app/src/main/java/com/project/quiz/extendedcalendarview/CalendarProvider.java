package com.project.quiz.extendedcalendarview;

import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.project.quiz.database.EventsAll;
import com.project.quiz.database.StorePointsTable;

public class CalendarProvider extends ContentProvider {

    private static final String DATABASE_NAME = "CalendarViewScrollable";
    private static final String EVENTS_TABLE = "events";
    private static final int DATABASE_VERSION = 4;


    private static final int TEXT_INSERT = 1;
    private static final int TEXT_ALL_EVENT = 6;
    private static final int TEXT_BULK_INSERT = 5;


    private static final String AUTHORITY = "com.project.quiz.extendedcalendarview.calendarprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/events");
    public static final Uri CONTENT_ID_URI_BASE = Uri.parse("content://" + AUTHORITY + "/events/");
    private static final String BASE_PATH_BULK_INSERT = "bulkInsert";
    private static final String BASE_PATH_ALL_EVENT = "copyEvent";

    public static final Uri CONTENT_BULK_INSERT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_BULK_INSERT);
    public static final Uri CONTENT_ALL_EVENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_ALL_EVENT);
    private static final UriMatcher uriMatcher;

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
    public static final String EVENT_SELECTED = "event_selected";

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTables(db);
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            Log.w("CalendarProvider", "Upgrading database from version " + oldVersion
                    + " to "
                    + newVersion + ", which will destroy all old data");
            EventsAll.onUpgrade(db, oldVersion, newVersion);
            db.execSQL("DROP TABLE IF EXISTS events");
            onCreate(db);
        }

        private void createTables(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + EVENTS_TABLE + "(" + ID + " integer primary key autoincrement, " +
                    EVENT + " TEXT, " + EVENT_ID + " TEXT UNIQUE, " + LOCATION + " TEXT, " + LOCATION_ID + " TEXT, " + DESCRIPTION + " TEXT, "
                    + START + " INTEGER, " + EVENT_SELECTED + " INTEGER default 0, " + END + " INTEGER, " + START_DAY + " INTEGER, " + END_DAY + " INTEGER, " + COLOR + " INTEGER);");
        }
    }

    public long getCountOfEvents(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        long c = DatabaseUtils.queryNumEntries(database, CalendarProvider.EVENTS_TABLE, null, null);
        database.close();
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        int num = uriMatcher.match(uri);
        if (num == TEXT_INSERT) {
            count = db.delete(EVENTS_TABLE, selection, selectionArgs);
            int count2 = db.delete(EventsAll.EVENTS_ALL_TABLE, selection, selectionArgs);
        } else if (num == 2) {
            String id = uri.getPathSegments().get(1);
            count = db.delete(EVENTS_TABLE, ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" +
                            selection + ')' : ""),
                    selectionArgs);
        }
        else if(num == TEXT_ALL_EVENT) {
            String query = "DROP TABLE IF EXISTS " + EventsAll.EVENTS_ALL_TABLE;
            db.execSQL(query);
            EventsAll.onCreate(db);
        }
        return count;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int num = uriMatcher.match(uri);
        switch (num) {
            case TEXT_INSERT :
                long rowID = db.insertWithOnConflict(EVENTS_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                if (rowID > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);

                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            case TEXT_ALL_EVENT:
                long row = db.insertWithOnConflict(EventsAll.EVENTS_ALL_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                getContext().getContentResolver().notifyChange(uri, null);
        }

        return uri;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();
        return (db == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();

        if (uriMatcher.match(uri) == 1) {
            sqlBuilder.setTables(EVENTS_TABLE);
        } else if (uriMatcher.match(uri) == 2) {
            sqlBuilder.setTables(EVENTS_TABLE);
            sqlBuilder.appendWhere(ID + "=?");
            selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs, new String[]{uri.getLastPathSegment()});
        } else if (uriMatcher.match(uri) == 3) {
            sqlBuilder.setTables(EVENTS_TABLE);
            sqlBuilder.appendWhere(START + ">=? OR ");
            sqlBuilder.appendWhere(END + "<=?");
            List<String> list = uri.getPathSegments();
            String start = list.get(1);
            String end = list.get(2);
            selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs, new String[]{start, end});
        }
        else if(uriMatcher.match(uri) == TEXT_ALL_EVENT){
            sqlBuilder.setTables(EventsAll.EVENTS_ALL_TABLE);
        }
        else
        if (sortOrder == null || sortOrder == "")
            sortOrder = START + " COLLATE LOCALIZED ASC";
        Cursor c = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        int num = uriMatcher.match(uri);
        if (num == 1) {
            count = db.update(EVENTS_TABLE, values, selection, selectionArgs);
        } else if (num == 2) {
            count = db.update(EVENTS_TABLE, values, ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" +
                            selection + ')' : ""),
                    selectionArgs);
        } else {
            throw new IllegalArgumentException(
                    "Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int retCount = 0;
        int num = uriMatcher.match(uri);
        if (num == TEXT_BULK_INSERT) {
            db.beginTransaction();
            try {
                //insert data
                for (ContentValues value : values) {
                    long _id = db.insertWithOnConflict(EventsAll.EVENTS_ALL_TABLE, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                    if (-1 != _id)
                        retCount++;
                }
                // set transaction to be successful
                db.setTransactionSuccessful();
            } finally {
                // end transaction
                db.endTransaction();
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        getContext().getContentResolver().notifyChange(CalendarProvider.CONTENT_ALL_EVENT_URI, null);
        return retCount;
    }


static{
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,EVENTS_TABLE,TEXT_INSERT);
        uriMatcher.addURI(AUTHORITY,EVENTS_TABLE+"/#",2);
        uriMatcher.addURI(AUTHORITY,EVENTS_TABLE+"/#/#",3);
        uriMatcher.addURI(AUTHORITY,BASE_PATH_BULK_INSERT,TEXT_BULK_INSERT);
        uriMatcher.addURI(AUTHORITY, BASE_PATH_ALL_EVENT, TEXT_ALL_EVENT);
        }

        }
