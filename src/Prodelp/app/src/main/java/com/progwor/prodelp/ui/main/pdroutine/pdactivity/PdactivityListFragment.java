package com.progwor.prodelp.ui.main.pdroutine.pdactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.progwor.prodelp.R;
import com.progwor.prodelp.core.Pdactivity;
import com.progwor.prodelp.core.Pdroutine;
import com.progwor.prodelp.core.Prodelp;
import com.progwor.prodelp.data.ProdelpContract;
import com.progwor.prodelp.data.ProdelpLoader;
import com.software.shell.fab.ActionButton;

public class PdactivityListFragment extends Fragment {

    //Unique IDs for loaders
    private static final int PDACTIVITY_LOADER = 0;
    // Specify the columns we need.
    private static final String[] PDACTIVITY_COLUMNS = {
            ProdelpContract.PdactivityEntry._ID,
            ProdelpContract.PdactivityEntry.NAME_COL,
            ProdelpContract.PdactivityEntry.START_TIME_COL,
            ProdelpContract.PdactivityEntry.END_TIME_COL
    };
    long pdroutineId;
    //CursorLoader for loading data
    private ProdelpLoader mProdelpLoader;
    private Bundle arguments;

    public PdactivityListFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        arguments = getArguments();
        pdroutineId = arguments.getLong(Pdroutine.ID_STRING);
        if (pdroutineId == 0) {
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            pdroutineId = Long.parseLong(sharedPref.getString(Pdroutine.ID_STRING, ""));
            arguments.putLong(Pdroutine.ID_STRING, pdroutineId);
        }

        //Toast.makeText(getActivity(), "id=" + pdroutineId, Toast.LENGTH_LONG).show();
        getLoaderManager().initLoader(PDACTIVITY_LOADER, arguments, mProdelpLoader);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Pdactivity pdactivity = new Pdactivity();
        mProdelpLoader = pdactivity.getLoaderCallbacks();
        View pdactivityView = pdactivity.load(getActivity(), inflater, container);

        ActionButton actionButton = (ActionButton) pdactivityView.findViewById(R.id.action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPdactivityActivity.class);
                intent.putExtra(Pdroutine.ID_STRING, arguments.getLong(Pdroutine.ID_STRING));
                intent.putExtra(Prodelp.ADD, true);
                getActivity().startActivity(intent);
            }
        });

        return pdactivityView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(Pdroutine.ID_STRING, arguments.getLong(Pdroutine.ID_STRING));
        super.onSaveInstanceState(outState);
        //Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveState();
    }

    void saveState() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Pdroutine.ID_STRING, arguments.getLong(Pdroutine.ID_STRING) + "");
        editor.apply();
    }
}