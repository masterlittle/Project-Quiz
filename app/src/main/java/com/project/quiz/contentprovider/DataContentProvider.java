package com.project.quiz.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.project.quiz.database.DataDatabaseHelper;
import com.project.quiz.database.StorePointsTable;
import com.project.quiz.database.StudentRecords;
import com.project.quiz.log.Logging;
import com.project.quiz.utils.CommonLibs;

/**
 * Created by Shitij on 25/07/15.
 */
public class DataContentProvider extends ContentProvider {


    // database
    private DataDatabaseHelper database;

    // used for the UriMatcher
    private static final int TEXT_STORE_DATA = 10;
    private static final int TEXT_STORE_STUDENTS = 40;
    private static final int TEXT_DATA_DELETE = 20;
    private static final int TEXT_DATA_BULK_INSERT = 30;
    private static final int TEXT_UPDATE_SCORE_STUDENTS = 50;

    private static final String AUTHORITY = "com.project.quiz.contentprovider";

    private static final String BASE_PATH_STORE = "tablePoints";
    private static final String BASE_PATH_STORE_STUDENTS = "storeStudents";
    private static final String BASE_PATH_UPDATE_SCORE_STUDENTS = "updateScoreStudents";
    private static final String BASE_PATH_DELETE = "tableDelete";
    private static final String BASE_PATH_BULK_INSERT = "bulkInsert";

    public static final Uri CONTENT_DELETE_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_DELETE);

    public static final Uri CONTENT_STORE_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_STORE);

    public static final Uri CONTENT_STORE_STUDENTS_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_STORE_STUDENTS);

    public static final Uri CONTENT_UPDATE_SCORE_STUDENTS_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_UPDATE_SCORE_STUDENTS);

    public static final Uri CONTENT_BULK_INSERT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_BULK_INSERT);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/finalappData";

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/finalappId";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_STORE, TEXT_STORE_DATA);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_STORE_STUDENTS, TEXT_STORE_STUDENTS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_UPDATE_SCORE_STUDENTS, TEXT_UPDATE_SCORE_STUDENTS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_BULK_INSERT, TEXT_DATA_BULK_INSERT);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_DELETE, TEXT_DATA_DELETE);
    }

    @Override
    public boolean onCreate() {
        database = new DataDatabaseHelper(getContext());
        return false;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        //checkColumns(projection);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case TEXT_STORE_DATA:
                // Set the table
                queryBuilder.setTables(StorePointsTable.TABLE_DATA_TEXT);

                break;

            case TEXT_STORE_STUDENTS:
                queryBuilder.setTables(StudentRecords.TABLE_DATA_TEXT);

                break;
        }

        SQLiteDatabase db = database.getWritableDatabase();

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder, null);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id;
        try {
            switch (uriType) {
                case TEXT_STORE_DATA:
                    id = sqlDB.insertWithOnConflict(StorePointsTable.TABLE_DATA_TEXT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    //id = sqlDB.insert(StoreDataTable.TABLE_DATA_TEXT, null, values);
                    break;
                case TEXT_STORE_STUDENTS:
                    id = sqlDB.insertWithOnConflict(StudentRecords.TABLE_DATA_TEXT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    //id = sqlDB.insert(StoreDataTable.TABLE_DATA_TEXT, null, values);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (Exception e) {
            Logging.logException("SQL", e, CommonLibs.Priority.MEDIUM);
        }
        return uri;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted;
        switch (uriType) {
            case TEXT_STORE_DATA:
                rowsDeleted = sqlDB.delete(StorePointsTable.TABLE_DATA_TEXT, selection,
                        selectionArgs);
                break;
            case TEXT_STORE_STUDENTS:
                rowsDeleted = sqlDB.delete(StudentRecords.TABLE_DATA_TEXT, selection,
                        selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case TEXT_DATA_DELETE:
//                    rowsDeleted = sqlDB.delete(StoreDataTable.TABLE_DATA_TEXT, null, null);
                String query = "DROP TABLE IF EXISTS " + StorePointsTable.TABLE_DATA_TEXT;
                sqlDB.execSQL(query);
                StorePointsTable.onCreate(sqlDB);
                rowsDeleted = 0;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
//            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String id;
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated;
        switch (uriType) {
            case TEXT_STORE_DATA:
                rowsUpdated = sqlDB.update(StorePointsTable.TABLE_DATA_TEXT,
                        values,
                        selection,
                        selectionArgs);
                break;
            case TEXT_STORE_STUDENTS:
                rowsUpdated = sqlDB.update(StudentRecords.TABLE_DATA_TEXT,
                        values,
                        selection,
                        selectionArgs);
                break;
            case TEXT_UPDATE_SCORE_STUDENTS:
                rowsUpdated = 0;
                sqlDB.execSQL("UPDATE " + StudentRecords.TABLE_DATA_TEXT + " SET " + StudentRecords.STUDENT_SCORE + " = " + StudentRecords.STUDENT_SCORE + values.get(StudentRecords.STUDENT_SCORE) + selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
//        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = database.getWritableDatabase();
        final int match = sURIMatcher.match(uri);
        int retCount = 0;
        switch (match) {
            case TEXT_DATA_BULK_INSERT:
                db.beginTransaction();
                try {
                    //insert data
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(StorePointsTable.TABLE_DATA_TEXT, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (-1 != _id)
                            retCount++;
                    }

                    // set transaction to be successful
                    db.setTransactionSuccessful();
                } finally {
                    // end transaction
                    db.endTransaction();
                }
                break;
        }
        return retCount;
    }

}

