package com.progwor.prodelp.core;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.progwor.prodelp.R;
import com.progwor.prodelp.data.ProdelpContract;
import com.progwor.prodelp.data.ProdelpLoader;
import com.progwor.prodelp.ui.main.pdroutine.AddPdroutineActivity;
import com.progwor.prodelp.ui.main.pdroutine.PdroutineListAdapter;
import com.progwor.prodelp.ui.main.pdroutine.pdactivity.PdactivityListActivity;
import com.software.shell.fab.ActionButton;

public class Pdroutine {

    public static final String ID_STRING = "pdroutineId";

    //Strings to identify the oprations
    public static final String ADD = "add";
    public static final String EDIT = "edit";

    //Projection columns for query
    public static final String[] COLS = {
            ProdelpContract.PdroutineEntry._ID,
            ProdelpContract.PdroutineEntry.NAME_COL,
            ProdelpContract.PdroutineEntry.START_DATE_COL,
            ProdelpContract.PdroutineEntry.END_DATE_COL
    };

    //CursorLoader
    private ProdelpLoader mProdelpLoader;

    //LoaderCallbacks required by CursorLoader
    public ProdelpLoader getLoaderCallbacks() {
        mProdelpLoader = new ProdelpLoader();
        return mProdelpLoader;
    }

    //Returns the cursor from id
    public Cursor getCursorById(Context context, long id) {
        Cursor cursor = context.getContentResolver()
                .query(ProdelpContract.PdroutineEntry.CONTENT_URI, COLS,
                        ProdelpContract.PdroutineEntry._ID + "=" + id, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    //Returns the cursor
    public Cursor getCursor(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(ProdelpContract.PdroutineEntry.CONTENT_URI, COLS, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    //Loads the routines into listview
    public View load(final Context context, LayoutInflater layoutInflater,
                     ViewGroup container, int selectedItem) {

        PdroutineListAdapter pdroutineListAdapter = new PdroutineListAdapter(context,
                null, 0);
        mProdelpLoader.setAdapter(context, pdroutineListAdapter, selectedItem);
        View pdroutineView = layoutInflater.inflate(R.layout.pdroutine_list_fragment, container,
                false);
        ListView listView = (ListView) pdroutineView.findViewById(R.id.pdroutine_listview);
        listView.setAdapter(pdroutineListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, PdactivityListActivity.class);
                intent.putExtra(ID_STRING, id);
                context.startActivity(intent);
            }
        });

        //Floating Action Button
        ActionButton actionButton = (ActionButton) pdroutineView.findViewById(R.id.action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddPdroutineActivity.class);
                intent.putExtra(Pdroutine.ADD, true);
                context.startActivity(intent);
            }
        });

        return pdroutineView;
    }

    //Adds new routine
    public void add(Context context, String name, String startDate, String endDate) {
        ContentValues routineValues = new ContentValues();
        routineValues.put(ProdelpContract.PdroutineEntry.NAME_COL, name);
        routineValues.put(ProdelpContract.PdroutineEntry.START_DATE_COL, startDate);
        routineValues.put(ProdelpContract.PdroutineEntry.END_DATE_COL, endDate);
        context.getContentResolver().insert(ProdelpContract.PdroutineEntry.CONTENT_URI,
                routineValues);

    }

    //Edits routine
    public void edit(Context context, long id, String name, String startDate, String endDate) {
        ContentValues routineValues = new ContentValues();
        routineValues.put(ProdelpContract.PdroutineEntry.NAME_COL, name);
        routineValues.put(ProdelpContract.PdroutineEntry.START_DATE_COL, startDate);
        routineValues.put(ProdelpContract.PdroutineEntry.END_DATE_COL, endDate);
        context.getContentResolver()
                .update(ProdelpContract.PdroutineEntry.CONTENT_URI, routineValues,
                        ProdelpContract.PdroutineEntry._ID + "=" + id, null);
    }

    //Deletes routine
    public void delete(Context context, long id) {
        context.getContentResolver()
                .delete(ProdelpContract.PdroutineEntry.CONTENT_URI,
                        ProdelpContract.PdroutineEntry._ID + "=" + id, null);
    }

}
