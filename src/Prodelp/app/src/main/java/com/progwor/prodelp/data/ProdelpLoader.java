package com.progwor.prodelp.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.CursorAdapter;

import com.progwor.prodelp.core.Pdactivity;
import com.progwor.prodelp.core.Pdgoal;
import com.progwor.prodelp.core.Pdroutine;
import com.progwor.prodelp.core.Prodelp;
import com.progwor.prodelp.ui.main.pdgoal.PdgoalListAdapter;
import com.progwor.prodelp.ui.main.pdreview.PdreviewListAdapter;
import com.progwor.prodelp.ui.main.pdroutine.PdroutineListAdapter;
import com.progwor.prodelp.ui.main.pdroutine.pdactivity.PdactivityListAdapter;

public class ProdelpLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    Context mContext;
    private PdroutineListAdapter mPdroutineListAdapter;
    private PdactivityListAdapter mPdactivityListAdapter;
    private PdgoalListAdapter mPdgoalListAdapter;
    private PdreviewListAdapter mPdreviewListAdapter;
    // Selected item from drawer
    private int mSelectedItem;

    // Set an adapter for selected item
    public void setAdapter(Context context, CursorAdapter cursorListAdapter, int selectedItem) {
        this.mContext = context;
        this.mSelectedItem = selectedItem;
        switch (mSelectedItem) {
            case Prodelp.PDROUTINE:
                this.mPdroutineListAdapter = (PdroutineListAdapter) cursorListAdapter;
                break;
            case Prodelp.PDGOAL:
                this.mPdgoalListAdapter = (PdgoalListAdapter) cursorListAdapter;
                break;
            case Prodelp.PDREVIEW:
                this.mPdreviewListAdapter = (PdreviewListAdapter) cursorListAdapter;
                break;
            case Prodelp.PDACTIVITY:
                this.mPdactivityListAdapter = (PdactivityListAdapter) cursorListAdapter;
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (mSelectedItem) {
            case Prodelp.PDROUTINE:
                Uri pdroutineUri = ProdelpContract.PdroutineEntry.CONTENT_URI;
                return new CursorLoader(mContext,
                        pdroutineUri,
                        Pdroutine.COLS,
                        null,
                        null,
                        null);
            case Prodelp.PDGOAL:
                Uri pdgoalUri = ProdelpContract.PdgoalEntry.CONTENT_URI;
                return new CursorLoader(mContext,
                        pdgoalUri,
                        Pdgoal.COLS,
                        null,
                        null,
                        null);
            case Prodelp.PDPROGRESS:
                return null;
            case Prodelp.PDREVIEW:
                long pdroutineId = args.getLong(Pdroutine.ID_STRING);
                Uri pdactivityUri = ProdelpContract.PdactivityEntry.CONTENT_URI;
                return new CursorLoader(mContext,
                        pdactivityUri,
                        Pdactivity.COLS,
                        ProdelpContract.PdactivityEntry.PDROUTINE_ID_COL + "=" + pdroutineId,
                        null,
                        null);

            case Prodelp.PDACTIVITY:
                return new CursorLoader(mContext,
                        ProdelpContract.PdactivityEntry.CONTENT_URI,
                        Pdactivity.COLS,
                        ProdelpContract.PdactivityEntry.PDROUTINE_ID_COL + "=" + args.getLong(Pdroutine.ID_STRING),
                        null,
                        null);

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (mSelectedItem) {
            case Prodelp.PDROUTINE:
                mPdroutineListAdapter.swapCursor(data);
                break;
            case Prodelp.PDGOAL:
                mPdgoalListAdapter.swapCursor(data);
                break;
            case Prodelp.PDPROGRESS:
                return;
            case Prodelp.PDREVIEW:
                mPdreviewListAdapter.swapCursor(data);
                break;
            case Prodelp.PDACTIVITY:
                mPdactivityListAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (mSelectedItem) {
            case Prodelp.PDROUTINE:
                mPdroutineListAdapter.swapCursor(null);
                break;
            case Prodelp.PDGOAL:
                mPdgoalListAdapter.swapCursor(null);
                break;
            case Prodelp.PDPROGRESS:
                return;
            case Prodelp.PDREVIEW:
                mPdreviewListAdapter.swapCursor(null);
                break;
            case Prodelp.PDACTIVITY:
                mPdactivityListAdapter.swapCursor(null);
                break;
        }
    }
}
