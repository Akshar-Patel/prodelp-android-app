package com.progwor.prodelp.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.progwor.prodelp.R;
import com.progwor.prodelp.data.ProdelpContract;
import com.progwor.prodelp.data.ProdelpLoader;
import com.progwor.prodelp.ui.main.pdroutine.pdactivity.PdactivityListAdapter;

public class Pdactivity {

    public static final String ID_STRING = "pdactivityId";

    //Projection columns for query
    public static final String[] COLS = {
            ProdelpContract.PdactivityEntry._ID,
            ProdelpContract.PdactivityEntry.NAME_COL,
            ProdelpContract.PdactivityEntry.START_TIME_COL,
            ProdelpContract.PdactivityEntry.END_TIME_COL,
            ProdelpContract.PdactivityEntry.PDROUTINE_ID_COL,
            ProdelpContract.PdactivityEntry.NOTIFY_COL
    };

    //CursorLoader
    private ProdelpLoader mProdelpLoader;

    //LoaderCallbacks required by CursorLoader
    public ProdelpLoader getLoaderCallbacks() {
        mProdelpLoader = new ProdelpLoader();
        return mProdelpLoader;
    }

    //Returns the cursor from id
    public Cursor getCursorByPdroutineId(Context context, long id) {
        Cursor cursor = context.getContentResolver()
                .query(ProdelpContract.PdactivityEntry.CONTENT_URI, COLS,
                        ProdelpContract.PdactivityEntry.PDROUTINE_ID_COL + "=" + id, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    //Returns the cursor from id
    public Cursor getCursorById(Context context, long id) {
        Cursor cursor = context.getContentResolver()
                .query(ProdelpContract.PdactivityEntry.CONTENT_URI, COLS,
                        ProdelpContract.PdactivityEntry._ID + "=" + id, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    //Loads the activities into listview
    public View load(Context context, LayoutInflater layoutInflater, ViewGroup container) {
        PdactivityListAdapter pdactivityListAdapter =
                new PdactivityListAdapter(context, null, 0);
        mProdelpLoader.setAdapter(context, pdactivityListAdapter, Prodelp.PDACTIVITY);
        View pdactivityView = layoutInflater.inflate(R.layout.pdactivity_list_fragment,
                container, false);
        ListView listView = (ListView) pdactivityView.findViewById(R.id.pdactivity_listview);
        listView.setAdapter(pdactivityListAdapter);
        return pdactivityView;
    }

    //Adds new activity
    public void add(Context context, String name, String startTime, String endTime, long routineId,
                    int notify) {
        ContentValues activityValues = new ContentValues();
        activityValues.put(ProdelpContract.PdactivityEntry.NAME_COL, name);
        activityValues.put(ProdelpContract.PdactivityEntry.START_TIME_COL, startTime);
        activityValues.put(ProdelpContract.PdactivityEntry.END_TIME_COL, endTime);
        activityValues.put(ProdelpContract.PdactivityEntry.PDROUTINE_ID_COL, routineId);
        activityValues.put(ProdelpContract.PdactivityEntry.NOTIFY_COL, notify);
        context.getContentResolver().insert(ProdelpContract.PdactivityEntry.CONTENT_URI,
                activityValues);
    }

    //Edits an activity
    public void edit(Context context, long id, String name, String startTime, String endTime,
                     int notify) {
        ContentValues activityValues = new ContentValues();
        activityValues.put(ProdelpContract.PdactivityEntry.NAME_COL, name);
        activityValues.put(ProdelpContract.PdactivityEntry.START_TIME_COL, startTime);
        activityValues.put(ProdelpContract.PdactivityEntry.END_TIME_COL, endTime);
        activityValues.put(ProdelpContract.PdactivityEntry.NOTIFY_COL, notify);
        context.getContentResolver()
                .update(ProdelpContract.PdactivityEntry.CONTENT_URI, activityValues,
                        ProdelpContract.PdactivityEntry._ID + "=" + id, null);

    }

    //Deletes an activity
    public void delete(Context context, long id) {
        context.getContentResolver()
                .delete(ProdelpContract.PdactivityEntry.CONTENT_URI,
                        ProdelpContract.PdactivityEntry._ID + "=" + id, null);
    }
}
