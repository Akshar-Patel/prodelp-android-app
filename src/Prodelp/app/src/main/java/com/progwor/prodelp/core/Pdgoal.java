package com.progwor.prodelp.core;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.progwor.prodelp.R;
import com.progwor.prodelp.data.ProdelpContract;
import com.progwor.prodelp.data.ProdelpLoader;
import com.progwor.prodelp.ui.main.pdgoal.AddPdgoalActivity;
import com.progwor.prodelp.ui.main.pdgoal.PdgoalListAdapter;
import com.software.shell.fab.ActionButton;

public class Pdgoal {

    public static final String ID_STRING = "pdgoalId";

    //Projection columns for query
    public static final String[] COLS = {
            ProdelpContract.PdgoalEntry._ID,
            ProdelpContract.PdgoalEntry.NAME_COL,
            ProdelpContract.PdgoalEntry.DUE_DATE_COL,
            ProdelpContract.PdgoalEntry.COMPLETED_DATE_COL,
            ProdelpContract.PdgoalEntry.PDACTIVITY_ID_COL
    };

    //CursorLoader
    private ProdelpLoader mProdelpLoader;

    //LoaderCallbacks required by CursorLoader
    public ProdelpLoader getLoaderCallbacks() {
        mProdelpLoader = new ProdelpLoader();
        return mProdelpLoader;
    }

    //Returns the cursor from id
    public Cursor getCursor(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(ProdelpContract.PdgoalEntry.CONTENT_URI, COLS,
                        null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getCursorByCompletedDate(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(ProdelpContract.PdgoalEntry.CONTENT_URI, COLS,
                        ProdelpContract.PdgoalEntry.COMPLETED_DATE_COL + "!=''", null, null);
        cursor.moveToFirst();
        return cursor;
    }

    //Returns the cursor from id
    public Cursor getCursorById(Context context, long id) {
        Cursor cursor = context.getContentResolver()
                .query(ProdelpContract.PdgoalEntry.CONTENT_URI, COLS,
                        ProdelpContract.PdgoalEntry._ID + "=" + id, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    //Loads the activities into listview
    public View load(final Context context, LayoutInflater layoutInflater, ViewGroup container) {
        PdgoalListAdapter pdgoalListAdapter =
                new PdgoalListAdapter(context, null, 0);
        mProdelpLoader.setAdapter(context, pdgoalListAdapter, Prodelp.PDGOAL);
        View pdgoalView = layoutInflater.inflate(R.layout.pdgoal_list_fragment,
                container, false);
        ListView listView = (ListView) pdgoalView.findViewById(R.id.pdgoal_listview);
        listView.setAdapter(pdgoalListAdapter);


        //Floating Action Button
        ActionButton actionButton = (ActionButton) pdgoalView.findViewById(R.id.action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddPdgoalActivity.class);
                intent.putExtra(Prodelp.ADD, true);
                context.startActivity(intent);
            }
        });
        return pdgoalView;
    }

    //Adds new routine
    public void add(Context context, String name, String dueDate, long activityId) {
        ContentValues goalValues = new ContentValues();
        goalValues.put(ProdelpContract.PdgoalEntry.NAME_COL, name);
        goalValues.put(ProdelpContract.PdgoalEntry.DUE_DATE_COL, dueDate);
        goalValues.put(ProdelpContract.PdgoalEntry.COMPLETED_DATE_COL, "");
        goalValues.put(ProdelpContract.PdgoalEntry.PDACTIVITY_ID_COL, activityId);
        context.getContentResolver().insert(ProdelpContract.PdgoalEntry.CONTENT_URI,
                goalValues);

    }

    public void edit(Context context, long goalId, String name, String dueDate, long activityId) {
        ContentValues goalValues = new ContentValues();
        goalValues.put(ProdelpContract.PdgoalEntry.NAME_COL, name);
        goalValues.put(ProdelpContract.PdgoalEntry.DUE_DATE_COL, dueDate);
        goalValues.put(ProdelpContract.PdgoalEntry.PDACTIVITY_ID_COL, activityId);

        context.getContentResolver()
                .update(ProdelpContract.PdgoalEntry.CONTENT_URI, goalValues,
                        ProdelpContract.PdgoalEntry._ID + "=" + goalId, null);
    }

    public void delete(Context context, long id) {
        context.getContentResolver()
                .delete(ProdelpContract.PdgoalEntry.CONTENT_URI,
                        ProdelpContract.PdgoalEntry._ID + "=" + id, null);
    }

    public void addCompletedDate(Context context, long id, String date) {
        ContentValues goalValues = new ContentValues();
        goalValues.put(ProdelpContract.PdgoalEntry.COMPLETED_DATE_COL, date);
        context.getContentResolver()
                .update(ProdelpContract.PdgoalEntry.CONTENT_URI, goalValues,
                        ProdelpContract.PdgoalEntry._ID + "=" + id, null);
    }
}
