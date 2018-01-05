package com.example.ant.tiptracker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.ant.tiptracker.data.TipsContract.TipsEntry;


/**
 * Created by Ant on 7/4/2017.
 */

public class TipProvider extends ContentProvider {
    //Private dbHelper object
    private TipDbHelper mTipDbHelper;

    /**
     * URI matcher code for the content URI for the tips table
     */
    private static final int TIPS = 100;

    /** URI matcher code for the content URI for a single week in the tips table */
    private static final int TIP_ID = 101;

    /**
     * UriMatcher object to match a content URI to corresponding code
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        sUriMatcher.addURI(TipsContract.CONTENT_AUTHORITY, TipsContract.PATH_TIPS, TIPS);
        sUriMatcher.addURI(TipsContract.CONTENT_AUTHORITY, TipsContract.PATH_TIPS + "/#", TIP_ID);
    }



    @Override
    public boolean onCreate() {
        mTipDbHelper = new TipDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // The database to query
        SQLiteDatabase db = mTipDbHelper.getReadableDatabase();

        // The cursor to return
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case TIPS:
                //Return cursor of the entire table query
                cursor = db.query(TipsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case TIP_ID:
                // Return cursor of specific work week
                selection = TipsEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(TipsEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // The database to insert a new row into
        SQLiteDatabase db = mTipDbHelper.getWritableDatabase();

        // Insert the ContentValues
        long newRowId = db.insert(TipsEntry.TABLE_NAME, null, values);

        // Notify all listeners that the data has changed
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, newRowId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // The database to delete from
        SQLiteDatabase db = mTipDbHelper.getWritableDatabase();

        int rowsDeleted; // The number of rows deleted

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TIPS:
                // Delete the whole table
                rowsDeleted = db.delete(TipsEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case TIP_ID:
                // Delete a specific work week
                selection = TipsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(TipsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // Notify listeners if one or more rows were deleted
        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        // The database to update
        SQLiteDatabase db = mTipDbHelper.getWritableDatabase();

        int rowsAffected; // The number of rows affected by the update

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TIPS:
                // Update the whole tips table
                rowsAffected = db.update(TipsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TIP_ID:
                // Update a specific row of the table
                selection = TipsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsAffected = db.update(TipsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }



        // Notify all listeners that the data has changed
        getContext().getContentResolver().notifyChange(uri, null);

        return rowsAffected;
    }
}
