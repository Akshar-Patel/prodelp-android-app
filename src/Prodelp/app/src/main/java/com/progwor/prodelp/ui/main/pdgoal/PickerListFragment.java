package com.progwor.prodelp.ui.main.pdgoal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.progwor.prodelp.R;
import com.progwor.prodelp.core.Pdactivity;
import com.progwor.prodelp.core.Pdroutine;
import com.progwor.prodelp.data.ProdelpContract;

/**
 * Created by akshar on 24/3/15.
 */
public class PickerListFragment extends DialogFragment {

    String mPdactivityName;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

    //long id = getArguments().getLong("id");
        LinearLayout linearLayout = new LinearLayout(getActivity());
        ListView listView = new ListView(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    if(getTag().equals("pdroutinePicker")){
        builder.setTitle("Select Routine");
        Pdroutine pdroutine = new Pdroutine();
        final Cursor cursor = pdroutine.getCursor(getActivity());
        String[] from = new String[]{ProdelpContract.PdroutineEntry.NAME_COL};
        int[] to = new int[]{android.R.id.text1};
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1,cursor,from,to,0);
        listView.setAdapter(simpleCursorAdapter);
        linearLayout.addView(listView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogFragment newFragment = new PickerListFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("pdroutineId",id);
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "pdactivityPicker");
                dismiss();
            }
        });
    }

        if(getTag().equals("pdactivityPicker")){
            builder.setTitle("Select Activity");
            Pdactivity pdactivity = new Pdactivity();
            Bundle bundle = getArguments();
            final Cursor cursor = pdactivity.getCursorByPdroutineId(getActivity(),bundle.getLong("pdroutineId"));
            String[] from = new String[]{ProdelpContract.PdactivityEntry.NAME_COL};
            int[] to = new int[]{android.R.id.text1};
            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(getActivity(),
                    android.R.layout.simple_list_item_1,cursor,from,to,0);
            listView.setAdapter(simpleCursorAdapter);
            linearLayout.addView(listView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Pdactivity pdactivity = new Pdactivity();
                    final Cursor cursor = pdactivity.getCursorById(getActivity(),id);
                    mPdactivityName = cursor.getString(ProdelpContract.PdactivityEntry.NAME_COL_INDEX);
                    Button activityButton = (Button) getActivity().findViewById(R.id.pdgoal_pdactivity_button);
                    activityButton.setText(mPdactivityName);
                    TextView activityIdTextView = (TextView) getActivity().findViewById(R.id.pdgoal_pdactivityid_textview);
                    activityIdTextView.setText(cursor.getLong(ProdelpContract.PdactivityEntry.ID_COL_INDEX)+"");
                    //Toast.makeText(getActivity(),cursor.getLong(ProdelpContract.PdactivityEntry.ID_COL_INDEX)+"",Toast.LENGTH_LONG).show();
                    dismiss();
                }
            });
        }

        builder.setView(linearLayout);
        return builder.create();
    }

    String getPdactivityName(){
        return mPdactivityName;
    }
}
