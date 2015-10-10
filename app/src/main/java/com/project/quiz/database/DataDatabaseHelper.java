package com.project.quiz.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.quiz.log.Logging;
import com.project.quiz.utils.CommonLibs;

/**
 * Created by Shitij on 25/07/15.
 */
public class DataDatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "quizapp.db";
    private static final int DATABASE_VERSION = 2;

    public DataDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            StorePointsTable.onCreate(database);
            StudentRecords.onCreate(database);
        } catch (Exception e) {
            Logging.logError("StoreDataTable", e.toString(), CommonLibs.Priority.VERY_HIGH);
        }
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {

        try {
            StorePointsTable.onUpgrade(database, oldVersion, newVersion);
            StudentRecords.onUpgrade(database,oldVersion, newVersion);
        } catch (Exception e) {
            Logging.logError("StoreDataTable", e.toString(), CommonLibs.Priority.VERY_HIGH);
        }
    }
}
