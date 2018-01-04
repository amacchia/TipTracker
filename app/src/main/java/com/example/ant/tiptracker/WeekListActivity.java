package com.example.ant.tiptracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ant.tiptracker.data.TipsContract.TipsEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.ant.tiptracker.R.string.delete;

public class WeekListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = WeekListActivity.class.getSimpleName();

    private static final int TIPS_LOADER = 0;
    private Dialog entryDialog;
    TipCursorAdapter mTipCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Up List View
        ListView tipsListView = (ListView) findViewById(R.id.tips_list_view);
        View emptyView = findViewById(R.id.empty_view);
        tipsListView.setEmptyView(emptyView);

        // Set Up Cursor Adapter with List View
        mTipCursorAdapter = new TipCursorAdapter(this, null);
        tipsListView.setAdapter(mTipCursorAdapter);

        // Start the weekly tip editor when an item in the list view is clicked
        tipsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent startWeeklyEditor = new Intent(WeekListActivity.this, EditWeek.class);

                Uri weekUri = ContentUris.withAppendedId(TipsEntry.CONTENT_URI, id);
                startWeeklyEditor.setData(weekUri);

                startActivity(startWeeklyEditor);
            }
        });

        tipsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Uri weekUri = ContentUris.withAppendedId(TipsEntry.CONTENT_URI, id);
                createDeletionDialog(weekUri);
                return true;
            }
        });


        //Start Loader
        getLoaderManager().initLoader(TIPS_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Delete all work weeks from database
            case R.id.delete_all_data:
                deleteAllEntries();
                break;
            // Add a work week to database
            case R.id.add_work_week:
                createEntryDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllEntries() {
        // Show dialog to confirm user wants to delete all weeks from db
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_weeks_dialog);
        builder.setPositiveButton(getString(delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete db
                int rowsDeleted = getContentResolver().delete(TipsEntry.CONTENT_URI, null, null);
                Log.v(TAG, rowsDeleted + " " + getString(R.string.weeks_deleted));
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });

        AlertDialog deleteDialog = builder.create();
        deleteDialog.show();
    }

    private void createEntryDialog() {
        entryDialog = new Dialog(this);
        entryDialog.setContentView(R.layout.dialog_layout);

        Button positiveButton = (Button) entryDialog.findViewById(R.id.positive_button);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertWeek();
                entryDialog.dismiss();
            }
        });

        Button negativeButton = (Button) entryDialog.findViewById(R.id.negative_button);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entryDialog.dismiss();
            }
        });

        entryDialog.show();
    }

    private void insertWeek() {
        // Get views of the entry dialog
        Calendar calendar = Calendar.getInstance();
        DatePicker datePicker = (DatePicker) entryDialog.findViewById(R.id.date_picker);
        EditText editText = (EditText) entryDialog.findViewById(R.id.tips_edit_text);

        // Set date of calendar from DatePicker
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int dayOfMonth = datePicker.getDayOfMonth();
        calendar.set(year, month, dayOfMonth);

        // Set Date and dayOfWeek from calendar
        Date dateOfWeek = calendar.getTime();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Get day of the week
        String daySelected = null;
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                daySelected = TipsEntry.COLUMN_MONDAY;
                break;
            case Calendar.TUESDAY:
                daySelected = TipsEntry.COLUMN_TUESDAY;
                break;
            case Calendar.WEDNESDAY:
                daySelected = TipsEntry.COLUMN_WEDNESDAY;
                break;
            case Calendar.THURSDAY:
                daySelected = TipsEntry.COLUMN_THURSDAY;
                break;
            case Calendar.FRIDAY:
                daySelected = TipsEntry.COLUMN_FRIDAY;
                break;
            case Calendar.SATURDAY:
                daySelected = TipsEntry.COLUMN_SATURDAY;
                break;
            case Calendar.SUNDAY:
                daySelected = TipsEntry.COLUMN_SUNDAY;
                break;
        }

        // Get tips received
        double tipsReceived;
        try {
            tipsReceived = Double.parseDouble(editText.getText().toString());
        } catch (NumberFormatException nfe) {
            Log.e(TAG, "EditText did not have a correctly formatted number", nfe);
            Toast.makeText(this, "Must Enter a Number", Toast.LENGTH_SHORT).show();
            return;
        }


        // Get date in desired String format from dateOfWeek
        String dateString = new SimpleDateFormat("MMM d, yyyy").format(dateOfWeek);

        // Insert information into database
        ContentValues values = new ContentValues();
        values.put(daySelected, tipsReceived);
        values.put(TipsEntry.COLUMN_DATE, dateString);

        Uri newUri = getContentResolver().insert(TipsEntry.CONTENT_URI, values);
        if (newUri == null) {
            Toast.makeText(this, R.string.error_saving, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.tips_saved, Toast.LENGTH_LONG).show();
        }

    }

    private void createDeletionDialog(final Uri weekToDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_week);
        builder.setPositiveButton(getString(delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteWeek(weekToDelete);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });

        AlertDialog deleteDialog = builder.create();
        deleteDialog.show();
    }

    private void deleteWeek(Uri weekToDelete) {
        int rowsDeleted = getContentResolver().delete(weekToDelete, null, null);

        if (rowsDeleted == 0) {
            Toast.makeText(this, getString(R.string.error_deleting_week),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.week_deleted),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                TipsEntry.CONTENT_URI,
                null,
                null,
                null,
                TipsEntry._ID + " DESC");   // Order in descending order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mTipCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTipCursorAdapter.swapCursor(null);
    }
}
