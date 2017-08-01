package com.progwor.prodelp.core;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.progwor.prodelp.R;
import com.progwor.prodelp.data.ProdelpContract;
import com.progwor.prodelp.data.ProdelpLoader;
import com.progwor.prodelp.ui.main.ContainerFragment;
import com.progwor.prodelp.ui.main.pdreview.PdreviewFragment;

public class Pdreview {

    public static final int PDACTIVITY = 0;
    public static final int PDGOAL = 1;

    //CursorLoader
    private ProdelpLoader mProdelpLoader;

    //LoaderCallbacks required by CursorLoader
    public ProdelpLoader getLoaderCallbacks() {
        mProdelpLoader = new ProdelpLoader();
        return mProdelpLoader;
    }

    //Loads activities into listview
    public View load(final Context context, final ContainerFragment fragment, LayoutInflater layoutInflater, final ViewGroup container,
                     int selectedItem) {

        View pdreviewView = layoutInflater.inflate(R.layout.pdreview_buttons,
                container, false);

        final Button pdroutineButton = (Button) pdreviewView.findViewById(R.id.pdreview_pdactivity_button);
        final Button pdgoalButton = (Button) pdreviewView.findViewById(R.id.pdreview_pdgoal_button);


        //Toast.makeText(context,+"",Toast.LENGTH_LONG).show();
        int color = ((ColorDrawable) pdroutineButton.getBackground()).getColor();
        if (color == context.getResources().getColor(R.color.primary_dark)) {

            ((ActionBarActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.pdreview_container, PdreviewFragment.newInstance(Pdreview.PDACTIVITY))
                    .commit();
        }

        pdroutineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((ColorDrawable) v.getBackground()).getColor()
                        == context.getResources().getColor(R.color.primary)) {
                    v.setBackgroundColor(context.getResources().getColor(R.color.primary_dark));
                    pdgoalButton.setBackgroundColor(context.getResources().getColor(R.color.primary));
                    ((ActionBarActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.pdreview_container, PdreviewFragment.newInstance(Pdreview.PDACTIVITY))
                            .commit();
                }
            }
        });


        pdgoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((ColorDrawable) v.getBackground()).getColor()
                        == context.getResources().getColor(R.color.primary)) {
                    v.setBackgroundColor(context.getResources().getColor(R.color.primary_dark));
                    pdroutineButton.setBackgroundColor(context.getResources().getColor(R.color.primary));
                    ((ActionBarActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.pdreview_container, PdreviewFragment.newInstance(Pdreview.PDGOAL))
                            .commit();
                }
            }
        });

        return pdreviewView;
    }

    public void insertPdactivity(Context context, int rate, String date) {
        ContentValues reviewValues = new ContentValues();
        reviewValues.put(ProdelpContract.PdprogressPdactivityEntry.RATE_COL, rate);
        reviewValues.put(ProdelpContract.PdprogressPdactivityEntry.DATE_COL, date);
        context.getContentResolver().insert(ProdelpContract.PdprogressPdactivityEntry.CONTENT_URI,
                reviewValues);
    }

    public void insertPdgoal(Context context, long id, String date) {
        ContentValues reviewValues = new ContentValues();
        reviewValues.put(ProdelpContract.PdgoalEntry.COMPLETED_DATE_COL, date);
        context.getContentResolver()
                .update(ProdelpContract.PdgoalEntry.CONTENT_URI, reviewValues,
                        ProdelpContract.PdgoalEntry._ID + "=" + id, null);
    }
}


