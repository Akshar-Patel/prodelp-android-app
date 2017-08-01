package com.progwor.prodelp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class ProdelpProvider extends ContentProvider {

    static final int PDROUTINE = 100;
    static final int PDACTIVITY = 200;
    static final int PDGOAL = 300;
    static final int PDPROGRESS_PDACTIVITY = 400;

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private ProdelpDbHelper mOpenHelper;

    static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found. The code passed into the constructor represents the code to return for the
        // root URI. It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ProdelpContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, ProdelpContract.PDROUTINE_PATH, PDROUTINE);
        matcher.addURI(authority, ProdelpContract.PDACTIVITY_PATH, PDACTIVITY);
        matcher.addURI(authority, ProdelpContract.PDGOAL_PATH, PDGOAL);
        matcher.addURI(authority, ProdelpContract.PDPROGRESS_PDACTIVITY_PATH, PDPROGRESS_PDACTIVITY);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ProdelpDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Here's the switch statement that, given a URI, will determine what kind of request
        // it is,and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case PDROUTINE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ProdelpContract.PdroutineEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case PDGOAL: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ProdelpContract.PdgoalEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case PDACTIVITY: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ProdelpContract.PdactivityEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case PDPROGRESS_PDACTIVITY: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ProdelpContract.PdprogressPdactivityEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PDROUTINE:
                return ProdelpContract.PdroutineEntry.CONTENT_TYPE;

            case PDGOAL:
                return ProdelpContract.PdgoalEntry.CONTENT_TYPE;

            case PDACTIVITY:
                return ProdelpContract.PdactivityEntry.CONTENT_TYPE;

            case PDPROGRESS_PDACTIVITY:
                return ProdelpContract.PdprogressPdactivityEntry.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case PDROUTINE: {
                long _id = db.insert(ProdelpContract.PdroutineEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = ProdelpContract.PdroutineEntry.buildRoutineUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case PDGOAL: {

                long _id = db.insert(ProdelpContract.PdgoalEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = ProdelpContract.PdgoalEntry.buildGoalUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case PDACTIVITY: {
                long _id = db.insert(ProdelpContract.PdactivityEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = ProdelpContract.PdactivityEntry.buildActivityUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;

            }

            case PDPROGRESS_PDACTIVITY: {
                long _id = db.insert(ProdelpContract.PdprogressPdactivityEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = ProdelpContract.PdprogressPdactivityEntry.buildPdprogressPdactivityUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case PDROUTINE:
                rowsDeleted = db.delete(
                        ProdelpContract.PdroutineEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case PDACTIVITY:
                rowsDeleted = db.delete(
                        ProdelpContract.PdactivityEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case PDGOAL:
                rowsDeleted = db.delete(
                        ProdelpContract.PdgoalEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case PDPROGRESS_PDACTIVITY:
                rowsDeleted = db.delete(
                        ProdelpContract.PdprogressPdactivityEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case PDROUTINE:
                rowsUpdated = db.update(ProdelpContract.PdroutineEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            case PDGOAL:
                rowsUpdated = db.update(ProdelpContract.PdgoalEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            case PDACTIVITY:
                rowsUpdated = db.update(ProdelpContract.PdactivityEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
