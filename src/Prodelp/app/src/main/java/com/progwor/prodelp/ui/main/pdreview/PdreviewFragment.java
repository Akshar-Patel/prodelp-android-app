package com.progwor.prodelp.ui.main.pdreview;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.progwor.prodelp.R;
import com.progwor.prodelp.core.Pdgoal;
import com.progwor.prodelp.core.Pdreview;
import com.progwor.prodelp.core.Pdroutine;
import com.progwor.prodelp.core.Prodelp;
import com.progwor.prodelp.data.ProdelpContract;
import com.progwor.prodelp.data.ProdelpLoader;
import com.progwor.prodelp.ui.main.MainActivity;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class PdreviewFragment extends Fragment {


    private static final String ARG_SELECTED_ITEM = "selected_item";

    //CursorLoader
    private ProdelpLoader mProdelpLoader;

    //Selected button
    int mButtonSelected;
    public static PdreviewFragment newInstance(int selectedItem) {

        PdreviewFragment fragment = new PdreviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SELECTED_ITEM, selectedItem);
        fragment.setArguments(args);
        return fragment;
    }

    //LoaderCallbacks required by CursorLoader
    public ProdelpLoader getLoaderCallbacks() {
        mProdelpLoader = new ProdelpLoader();
        return mProdelpLoader;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProdelpLoader = new ProdelpLoader();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater layoutInflater, final ViewGroup container, Bundle savedInstanceState) {

        mButtonSelected = getArguments().getInt(ARG_SELECTED_ITEM);
        if (mButtonSelected == Pdreview.PDACTIVITY) {
            final PdreviewListAdapter pdreviewListAdapter =
                    new PdreviewListAdapter(getActivity(), null, 0);
            mProdelpLoader.setAdapter(getActivity(), pdreviewListAdapter, Prodelp.PDREVIEW);
            View pdreviewView = layoutInflater.inflate(R.layout.pdreview_pdactivity_list_fragment,
                    container, false);
            final ListView listView = (ListView) pdreviewView.findViewById(R.id.pdreview_pdroutine_listview);
            listView.setAdapter(pdreviewListAdapter);

            Spinner spinner = (Spinner) pdreviewView.findViewById(R.id.pdreview_spinner);
            final TextView textView = new TextView(getActivity());
            textView.setText("");
            Pdroutine pdroutine = new Pdroutine();
            Cursor cursor = pdroutine.getCursor(getActivity());
            String[] from = new String[]{ProdelpContract.PdroutineEntry.NAME_COL};
            // create an array of the display item we want to bind our data to
            int[] to = new int[]{R.id.pdreview_spinner_textview};
            SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.pdreview_spinner, cursor, from, to, 0);
            //mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            if(mAdapter.getCount()==0){
                return textView;
            }
            spinner.setAdapter(mAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    bundle.putLong(Pdroutine.ID_STRING, id);
                    getLoaderManager().restartLoader(Prodelp.LOADER, bundle, mProdelpLoader);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            final Button okButtonPdactivity = (Button) pdreviewView.findViewById(R.id.pdreview_pdactivity_ok_button);

            okButtonPdactivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String[] drawerItems = getResources().getStringArray(R.array.drawer_list);
                    ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(drawerItems[Prodelp.PDPROGRESS]);
                    ((MainActivity) getActivity()).mDrawerFragment.selectItem(Prodelp.PDPROGRESS);

                    int trueCount = 0;
                    Set<String> falseValues = new HashSet<String>();
                    for (int i = 0; i < pdreviewListAdapter.getCount(); i++) {
                        CheckBox pdactivityCheckBox = (CheckBox) listView.getChildAt(i).findViewById(R.id.pdreview_listview_item_checkbox);
                        if (pdactivityCheckBox.isChecked()) trueCount++;
                        else {
                            falseValues.add(pdactivityCheckBox.getText().toString());
                        }
                    }
                    if (!falseValues.isEmpty()) {
                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putStringSet("reviewFalseValues", falseValues);
                      editor.apply();
                    }

                    int rate = (int) (((double) trueCount / pdreviewListAdapter.getCount()) * 100.0);

                    final Calendar c = Calendar.getInstance();

                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH)+1;
                    int day = c.get(Calendar.DAY_OF_MONTH);


                    String[] days = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

                    String dayName = days[c.get(Calendar.DAY_OF_WEEK)-1];
                   // Toast.makeText(getActivity(),c.get(Calendar.DAY_OF_WEEK)+"",Toast.LENGTH_LONG).show();
                    String date = day+"/"+month+"/"+year;
                   // Toast.makeText(getActivity(), rate + "%", Toast.LENGTH_LONG).show();

                    //((MainActivity)getActivity()).onNavigationDrawerItemSelected(3);

                    Pdreview pdreview = new Pdreview();
                    pdreview.insertPdactivity(getActivity(), rate, date);

                }
            });

            return pdreviewView;
        }
        if(mButtonSelected == Pdreview.PDGOAL){

            final Pdgoal pdgoal = new Pdgoal();
            Cursor cursor = pdgoal.getCursor(getActivity());
            final TextView textView = new TextView(getActivity());
            textView.setText("");
            String[] from = new String[]{ProdelpContract.PdgoalEntry.NAME_COL,ProdelpContract.PdgoalEntry.DUE_DATE_COL};

            // create an array of the display item we want to bind our data to
            int[] to = new int[]{R.id.pdreview_listview_item_checkbox,R.id.pdreview_listview_item_textview};

            final SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.pdreview_listview_item, cursor, from, to, 0);
            //mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            if(mAdapter.getCount()==0){
                return textView;
            }
            View pdreviewView = layoutInflater.inflate(R.layout.pdreview_pdgoal_list_fragment,
                    container, false);
            final ListView listView = (ListView) pdreviewView.findViewById(R.id.pdreview_pdgoal_listview);
            listView.setAdapter(mAdapter);

            final Button okButtonPdgoal = (Button) pdreviewView.findViewById(R.id.pdreview_pdactivity_ok_button);
            okButtonPdgoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] drawerItems = getResources().getStringArray(R.array.drawer_list);
                    ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(drawerItems[Prodelp.PDPROGRESS]);
                    ((MainActivity) getActivity()).mDrawerFragment.selectItem(Prodelp.PDPROGRESS);

                    int trueCount = 0;

                    Set<String> falseValues = new HashSet<String>();
                    Pdreview pdreview = new Pdreview();

                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH)+1;
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    String date = day + "/" + month + "/" + year;

                    for (int i = 0; i < mAdapter.getCount(); i++) {
                        CheckBox pdgoalCheckBox = (CheckBox) listView.getChildAt(i).findViewById(R.id.pdreview_listview_item_checkbox);
                        if (pdgoalCheckBox.isChecked())
                        {
                            pdreview.insertPdgoal(getActivity(),listView.getAdapter().getItemId(i),date);
                        }
                        else {
                            falseValues.add(pdgoalCheckBox.getText().toString());
                        }
                    }

                    if (!falseValues.isEmpty()) {
                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putStringSet("reviewFalseValues", falseValues);
                        editor.apply();
                    }

                }
            });

            return pdreviewView;
        }
     return null;
    }
}
