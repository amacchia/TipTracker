package com.example.ant.tiptracker;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.ant.tiptracker.data.TipsContract.TipsEntry;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Anthony Macchia on 7/12/2017.
 */

public class TipCursorAdapter extends CursorAdapter {

    private static final String LOG_TAG = TipCursorAdapter.class.getSimpleName();

    // Formatter for the dates
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");

    // Formatter to display the tips with two decimal places
    private DecimalFormat decimalFormatter = new DecimalFormat("0.00");

    public TipCursorAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.week_info_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //TextViews to display info on in ListView
        TextView tvWeek = (TextView) view.findViewById(R.id.week_textview);
        TextView tvMoney = (TextView) view.findViewById(R.id.money_textview);

        //Get the date from the cursor
        String dateString = cursor.getString(cursor.getColumnIndexOrThrow(TipsEntry.COLUMN_DATE));
        Date date;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            date = null;
            Log.e(LOG_TAG, "Error converting String to Date", e);
            e.printStackTrace();
        }

        //Get the amount made on each day if anything, then add to get total made for the week
        double weeklyTotal;

        double mondayTotal = cursor.getDouble(cursor.getColumnIndexOrThrow(TipsEntry.COLUMN_MONDAY));
        double tuesdayTotal = cursor.getDouble(cursor.getColumnIndexOrThrow(TipsEntry.COLUMN_TUESDAY));
        double wednesdayTotal = cursor.getDouble(cursor.getColumnIndexOrThrow(TipsEntry.COLUMN_WEDNESDAY));
        double thursdayTotal = cursor.getDouble(cursor.getColumnIndexOrThrow(TipsEntry.COLUMN_THURSDAY));
        double fridayTotal = cursor.getDouble(cursor.getColumnIndexOrThrow(TipsEntry.COLUMN_FRIDAY));
        double saturdayTotal = cursor.getDouble(cursor.getColumnIndexOrThrow(TipsEntry.COLUMN_SATURDAY));
        double sundayTotal = cursor.getDouble(cursor.getColumnIndexOrThrow(TipsEntry.COLUMN_SUNDAY));

        // Sum the days of the week to get the total
        weeklyTotal = mondayTotal + tuesdayTotal + wednesdayTotal + thursdayTotal + fridayTotal
                + saturdayTotal + sundayTotal;

        String weekID;
        if (cursor.isFirst())
            // Set the latest week to current week
            weekID = context.getString(R.string.current_week);
        else
            weekID = context.getResources().getString(R.string.week_id) + dateFormat.format(date);

        // The weekly total
        String weekTotal = context.getResources().getString(R.string.dollar_sign) + decimalFormatter.format(weeklyTotal);

        tvWeek.setText(weekID);
        tvMoney.setText(weekTotal);
    }
}
