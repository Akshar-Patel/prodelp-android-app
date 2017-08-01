package com.progwor.prodelp.ui.main.pdgoal;

import android.app.Service;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.progwor.prodelp.R;
import com.progwor.prodelp.core.Pdactivity;
import com.progwor.prodelp.core.Pdgoal;
import com.progwor.prodelp.core.Pdroutine;
import com.progwor.prodelp.data.ProdelpContract;
import com.progwor.prodelp.ui.main.FetchTask;
import com.progwor.prodelp.ui.main.pdroutine.*;

public class AddPdgoalFragment extends Fragment {

    boolean mEdit;
    boolean mAdd;
    private EditText mPdgoalNameEditText;
    private Button mDueDateButton, mPdactivityButton;
    private TextView mPdactivityIdTextView;
    private long mPdactivityId,mPdgoalId;

    public AddPdgoalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPdgoalId = getActivity().getIntent().getLongExtra(Pdgoal.ID_STRING, 0);
        mAdd = getActivity().getIntent().getBooleanExtra(Pdroutine.ADD, false);
        mEdit = getActivity().getIntent().getBooleanExtra(Pdroutine.EDIT, false);

        //Toast.makeText(this, mAdd + "", Toast.LENGTH_SHORT).show();

        if (mEdit) ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(R.string.edit_pdgoal_activity_title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_accept) {



            if (mPdgoalNameEditText.getText().toString().trim().equals("")) {
                mPdgoalNameEditText.setError("You must enter Goal Name");
                return true;
            }

            if (mAdd) {
                if (mDueDateButton.getText().toString().equals(getResources().getString(R.string.add_pdgoal_due_date_button))) {
                    mDueDateButton.setError("You must choose Due Date");
                    return true;
                }

                if(mPdactivityIdTextView.getText().equals("")){
                    mPdactivityButton.setError("You must choose Activity.");
                    return true;
                }
                mPdactivityId = Long.parseLong(mPdactivityIdTextView.getText()+"");
                //Insert values
                Pdgoal pdgoal = new Pdgoal();
                pdgoal.add(getActivity(), mPdgoalNameEditText.getText().toString(),
                        mDueDateButton.getText().toString(), mPdactivityId);
            }

            if(mEdit){
                mPdactivityId = Long.parseLong(mPdactivityIdTextView.getText()+"");
                Pdgoal pdgoal = new Pdgoal();
                pdgoal.edit(getActivity(),mPdgoalId,mPdgoalNameEditText.getText().toString(),
                        mDueDateButton.getText().toString(), mPdactivityId);
            }
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pdgoal_add_pdgoal_fragment, container, false);

        mPdgoalNameEditText = (EditText) rootView.findViewById(R.id.pdgoal_name_edittext);
        mDueDateButton = (Button) rootView.findViewById(R.id.pdgoal_due_date_button);
        mPdactivityButton = (Button) rootView.findViewById(R.id.pdgoal_pdactivity_button);
        mPdactivityIdTextView = (TextView) rootView.findViewById(R.id.pdgoal_pdactivityid_textview);

        mPdgoalNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    if(!mPdgoalNameEditText.getText().toString().equals("")){
                        RelativeLayout relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.add_pdgoal_fragment);
                        FetchTask fetchTask = new FetchTask(getActivity(),relativeLayout,relativeLayout.findViewById(R.id.pdgoal_pdactivity_button).getId());
                        fetchTask.execute("how to "+mPdgoalNameEditText.getText());
                    }
                }
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow( mPdgoalNameEditText.getWindowToken(), 0);
                return true;
            }
        });

        mDueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(),"");

                //recommendLoaded
                RelativeLayout relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.add_pdgoal_fragment);
                FetchTask fetchTask = new FetchTask(getActivity(),relativeLayout,relativeLayout.findViewById(R.id.pdgoal_pdactivity_button).getId());
                fetchTask.execute("how to "+mPdgoalNameEditText.getText());

                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow( mPdgoalNameEditText.getWindowToken(), 0);

            }
        });

        mPdactivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"Dialog Picker",Toast.LENGTH_LONG);
                DialogFragment newFragment = new PickerListFragment();
                newFragment.show(getFragmentManager(),"pdroutinePicker");
            }
        });

        if(mEdit){
            Pdgoal pdgoal = new Pdgoal();
            Cursor cursor = pdgoal.getCursorById(getActivity(), mPdgoalId);
            mPdgoalNameEditText.setText(cursor.getString(ProdelpContract.PdroutineEntry.NAME_COL_INDEX));
            mDueDateButton.setText(cursor.getString(ProdelpContract.PdgoalEntry.DUE_DATE_COL_INDEX));

            long activityId = cursor.getLong(ProdelpContract.PdgoalEntry.PDACTIVITY_ID_COL_INDEX);

            if(activityId!=0){
                Pdactivity pdactivity = new Pdactivity();
                Cursor activitycursor = pdactivity.getCursorById(getActivity(),activityId);
                String pdactivityName = activitycursor.getString(ProdelpContract.PdactivityEntry.NAME_COL_INDEX);
                mPdactivityButton.setText(pdactivityName);
                mPdactivityIdTextView.setText(cursor.getString(ProdelpContract.PdgoalEntry.PDACTIVITY_ID_COL_INDEX));
            }

        }

        if (savedInstanceState != null) {
            // Restore values from Instance
            mDueDateButton.setText(savedInstanceState.getCharSequence("dueButton"));
           mPdactivityButton.setText(savedInstanceState.getCharSequence("activityButton"));
        }

        return rootView;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence("dueButton", mDueDateButton.getText());
        outState.putCharSequence("activityButton", mPdactivityButton.getText());
        super.onSaveInstanceState(outState);
    }
}