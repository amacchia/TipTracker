package com.example.ant.tiptracker;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ant.tiptracker.data.TipsContract.TipsEntry;

import java.text.DecimalFormat;

import static java.lang.Double.parseDouble;


public class EditWeek extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private Uri mCurrentWeekUri;
    private static final int EXISTING_TIPS_LOADER = 0; // Id of the tips loader

    //Edit text fields
    private EditText mMondayEditText;
    private EditText mTuesdayEditText;
    private EditText mWednesdayEditText;
    private EditText mThursdayEditText;
    private EditText mFridayEditText;
    private EditText mSaturdayEditText;
    private EditText mSundayEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_week);

        // Title of the action bar
        setTitle(getString(R.string.edit_week_title));

        //Examine intent used to launch activity to get id of week
        Intent intent = getIntent();
        mCurrentWeekUri = intent.getData();

        // Initialize edit text fields
        mMondayEditText = (EditText) findViewById(R.id.monday_edit_text);
        mTuesdayEditText = (EditText) findViewById(R.id.tuesday_edit_text);
        mWednesdayEditText = (EditText) findViewById(R.id.wednesday_edit_text);
        mThursdayEditText = (EditText) findViewById(R.id.thursday_edit_text);
        mFridayEditText = (EditText) findViewById(R.id.friday_edit_text);
        mSaturdayEditText = (EditText) findViewById(R.id.saturday_edit_text);
        mSundayEditText = (EditText) findViewById(R.id.sunday_edit_text);

        // Launch loader
        getLoaderManager().initLoader(EXISTING_TIPS_LOADER, null, this);
    }

    private void saveWeek(){
        double mondayTips;
        double tuesdayTips;
        double wednesdayTips;
        double thursdayTips;
        double fridayTips;
        double saturdayTips;
        double sundayTips;

        // Try to get values from edit text fields. If no new info is entered there is only a hint and text is blank
        try{
            mondayTips = parseDouble(mMondayEditText.getText().toString());
        } catch (NumberFormatException error){
            mondayTips = parseDouble(mMondayEditText.getHint().toString());
        }

        try{
            tuesdayTips = parseDouble(mTuesdayEditText.getText().toString());
        } catch (NumberFormatException error){
            tuesdayTips = parseDouble(mTuesdayEditText.getHint().toString());
        }

        try{
            wednesdayTips = parseDouble(mWednesdayEditText.getText().toString());
        } catch (NumberFormatException error){
            wednesdayTips = parseDouble(mWednesdayEditText.getHint().toString());
        }

        try{
            thursdayTips = parseDouble(mThursdayEditText.getText().toString());
        } catch (NumberFormatException error){
            thursdayTips = parseDouble(mThursdayEditText.getHint().toString());
        }

        try{
            fridayTips = parseDouble(mFridayEditText.getText().toString());
        } catch (NumberFormatException error){
            fridayTips = parseDouble(mFridayEditText.getHint().toString());
        }

        try{
            saturdayTips = parseDouble(mSaturdayEditText.getText().toString());
        } catch (NumberFormatException error){
            saturdayTips = parseDouble(mSaturdayEditText.getHint().toString());
        }

        try{
            sundayTips = parseDouble(mSundayEditText.getText().toString());
        } catch (NumberFormatException error){
            sundayTips = parseDouble(mSundayEditText.getHint().toString());
        }



        ContentValues values = new ContentValues();
        values.put(TipsEntry.COLUMN_MONDAY, mondayTips);
        values.put(TipsEntry.COLUMN_TUESDAY, tuesdayTips);
        values.put(TipsEntry.COLUMN_WEDNESDAY, wednesdayTips);
        values.put(TipsEntry.COLUMN_THURSDAY, thursdayTips);
        values.put(TipsEntry.COLUMN_FRIDAY, fridayTips);
        values.put(TipsEntry.COLUMN_SATURDAY, saturdayTips);
        values.put(TipsEntry.COLUMN_SUNDAY, sundayTips);

        // Update the work week in the database
        int rowsAffected = getContentResolver().update(mCurrentWeekUri, values, null, null);
        if (rowsAffected == 0)
            Toast.makeText(this, R.string.error_saving, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, R.string.tips_saved, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create options menu
        getMenuInflater().inflate(R.menu.edit_week_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_work_week:
                saveWeek();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Return new cursor of the current week
        String projection[] = {
                TipsEntry._ID,
                TipsEntry.COLUMN_MONDAY,
                TipsEntry.COLUMN_TUESDAY,
                TipsEntry.COLUMN_WEDNESDAY,
                TipsEntry.COLUMN_THURSDAY,
                TipsEntry.COLUMN_FRIDAY,
                TipsEntry.COLUMN_SATURDAY,
                TipsEntry.COLUMN_SUNDAY};

        return new CursorLoader(this,
                mCurrentWeekUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Move to the first row (only row) of the cursor and get the index of the columns
        Log.v("EditWeek", DatabaseUtils.dumpCursorToString(cursor));
        if (cursor.moveToFirst()) {
            // Formatter to display the tips with two decimal places
            DecimalFormat decimalFormatter = new DecimalFormat("0.00");

            // Get column indexes from cursor
            int mondayColumnIndex = cursor.getColumnIndex(TipsEntry.COLUMN_MONDAY);
            int tuesdayColumnIndex = cursor.getColumnIndex(TipsEntry.COLUMN_TUESDAY);
            int wednesdayColumnIndex = cursor.getColumnIndex(TipsEntry.COLUMN_WEDNESDAY);
            int thursdayColumnIndex = cursor.getColumnIndex(TipsEntry.COLUMN_THURSDAY);
            int fridayColumnIndex = cursor.getColumnIndex(TipsEntry.COLUMN_FRIDAY);
            int saturdayColumnIndex = cursor.getColumnIndex(TipsEntry.COLUMN_SATURDAY);
            int sundayColumnIndex = cursor.getColumnIndex(TipsEntry.COLUMN_SUNDAY);

            // Get tips from cursor
            double mondayTips = cursor.getDouble(mondayColumnIndex);
            double tuesdayTips = cursor.getDouble(tuesdayColumnIndex);
            double wednesdayTips = cursor.getDouble(wednesdayColumnIndex);
            double thursdayTips = cursor.getDouble(thursdayColumnIndex);
            double fridayTips = cursor.getDouble(fridayColumnIndex);
            double saturdayTips = cursor.getDouble(saturdayColumnIndex);
            double sundayTips = cursor.getDouble(sundayColumnIndex);

            // Set the tips made on each day to the hint of the corresponding text view
            mMondayEditText.setHint(decimalFormatter.format(mondayTips));
            mTuesdayEditText.setHint(decimalFormatter.format(tuesdayTips));
            mWednesdayEditText.setHint(decimalFormatter.format(wednesdayTips));
            mThursdayEditText.setHint(decimalFormatter.format(thursdayTips));
            mFridayEditText.setHint(decimalFormatter.format(fridayTips));
            mSaturdayEditText.setHint(decimalFormatter.format(saturdayTips));
            mSundayEditText.setHint(decimalFormatter.format(sundayTips));
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMondayEditText.setText("");
        mTuesdayEditText.setText("");
        mWednesdayEditText.setText("");
        mThursdayEditText.setText("");
        mFridayEditText.setText("");
        mSaturdayEditText.setText("");
        mSundayEditText.setText("");
    }
}
