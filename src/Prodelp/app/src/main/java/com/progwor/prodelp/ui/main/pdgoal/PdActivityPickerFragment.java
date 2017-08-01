package com.progwor.prodelp.ui.main.pdgoal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.progwor.prodelp.R;
import com.progwor.prodelp.core.Pdroutine;
import com.progwor.prodelp.data.ProdelpContract;

import java.util.List;

public class PdActivityPickerFragment extends DialogFragment{
     public PdActivityPickerFragment(){

     }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Pdroutine pdroutine = new Pdroutine();
        final Cursor cursor = pdroutine.getCursor(getActivity());
        String[] from = new String[]{ProdelpContract.PdroutineEntry.NAME_COL};
        int[] to = new int[]{android.R.id.text1};
        SimpleCursorAdapter  simpleCursorAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1,cursor,from,to);

        LinearLayout ll = new LinearLayout(getActivity());
        ListView lv = new ListView(getActivity());
        lv.setAdapter(simpleCursorAdapter);
        ll.addView(lv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(ll);
        return builder.create();
    }


}

