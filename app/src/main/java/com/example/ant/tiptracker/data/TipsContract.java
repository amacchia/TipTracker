package com.example.ant.tiptracker.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Anthony Macchia on 7/1/2017.
 */

public class TipsContract {

    private TipsContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.ant.tiptracker";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_TIPS = "tips";

    public static abstract class TipsEntry implements BaseColumns{
        /** The content URI to access the tip data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TIPS);

        /** The MIME type for a list of tips */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TIPS;

        /** The MIME type for a list of tips */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TIPS;

        /** The name of the table */
        public static final String TABLE_NAME = "tips";

        /** The column names for the table */
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_MONDAY = "monday";
        public static final String COLUMN_TUESDAY = "tuesday";
        public static final String COLUMN_WEDNESDAY = "wednesday";
        public static final String COLUMN_THURSDAY = "thursday";
        public static final String COLUMN_FRIDAY = "friday";
        public static final String COLUMN_SATURDAY = "saturday";
        public static final String COLUMN_SUNDAY = "sunday";
        public static final String COLUMN_DATE = "date";
    }





}
