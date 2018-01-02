package com.example.ant.tiptracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ant.tiptracker.data.TipsContract.TipsEntry;

/**
 * Created by Ant on 7/3/2017.
 */

public class TipDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = TipDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "tips.db";
    private static final int DATABASE_VERSION = 1;

    public TipDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tips table
        String SQL_CREATE_TIPS_TABLE = "CREATE TABLE " + TipsEntry.TABLE_NAME + " (" +
                TipsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TipsEntry.COLUMN_MONDAY + " INTEGER DEFAULT 0, " +
                TipsEntry.COLUMN_TUESDAY+ " INTEGER DEFAULT 0, " +
                TipsEntry.COLUMN_WEDNESDAY + " INTEGER DEFAULT 0, " +
                TipsEntry.COLUMN_THURSDAY + " INTEGER DEFAULT 0, " +
                TipsEntry.COLUMN_FRIDAY + " INTEGER DEFAULT 0, " +
                TipsEntry.COLUMN_SATURDAY + " INTEGER DEFAULT 0, " +
                TipsEntry.COLUMN_SUNDAY + " INTEGER DEFAULT 0, " +
                TipsEntry.COLUMN_DATE + " TEXT);";

        //Log create statement
        Log.v(LOG_TAG, SQL_CREATE_TIPS_TABLE);

        db.execSQL(SQL_CREATE_TIPS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
