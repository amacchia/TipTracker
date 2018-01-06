package com.example.ant.tiptracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ant.tiptracker.data.TipsContract.TipsEntry;

/**
 * Created by Anthony Macchia on 7/3/2017.
 */

public class TipDbHelper extends SQLiteOpenHelper {
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
                TipsEntry.COLUMN_MONDAY + " REAL DEFAULT 0, " +
                TipsEntry.COLUMN_TUESDAY+ " REAL DEFAULT 0, " +
                TipsEntry.COLUMN_WEDNESDAY + " REAL DEFAULT 0, " +
                TipsEntry.COLUMN_THURSDAY + " REAL DEFAULT 0, " +
                TipsEntry.COLUMN_FRIDAY + " REAL DEFAULT 0, " +
                TipsEntry.COLUMN_SATURDAY + " REAL DEFAULT 0, " +
                TipsEntry.COLUMN_SUNDAY + " REAL DEFAULT 0, " +
                TipsEntry.COLUMN_DATE + " TEXT);";

        db.execSQL(SQL_CREATE_TIPS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Only one version of tips database so far
    }
}
