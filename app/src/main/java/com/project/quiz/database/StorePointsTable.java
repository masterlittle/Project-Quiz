package com.project.quiz.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.project.quiz.log.Logging;
import com.project.quiz.utils.CommonLibs;

/**
 * Created by Shitij on 28/07/15.
 */
public class StorePointsTable {
    public static final String TABLE_DATA_TEXT = "table_points";
    public static final String TEAM_NUMBER = "team_number";
    public static final String CURRENT_SCORE = "current_score";
    public static final String CHANGED_SCORE = "changed_score";
    public static final String COLUMN_ID = "_id";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_DATA_TEXT
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + TEAM_NUMBER + " text not null unique, "
            + CURRENT_SCORE + " float, "
            + CHANGED_SCORE + " float "
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        try {
            database.execSQL(DATABASE_CREATE);
        }
        catch (Exception e){
            Logging.logError("StoreDataTable", e.toString(), CommonLibs.Priority.VERY_HIGH);
        }
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion)throws Exception {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA_TEXT);
        onCreate(database);
    }
}
