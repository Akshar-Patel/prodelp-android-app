package com.progwor.prodelp.ui.main.pdroutine.pdactivity;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.progwor.prodelp.R;
import com.progwor.prodelp.core.Pdactivity;
import com.progwor.prodelp.core.Pdroutine;
import com.progwor.prodelp.core.Prodelp;
import com.progwor.prodelp.data.ProdelpContract;
import com.progwor.prodelp.ui.main.FetchTask;

public class AddPdactivityFragment extends Fragment {

    boolean mAdd;
    boolean mEdit;
    private Button mStartTimeButton, mEndTimeButton, mNotifyTimeButton;
    private EditText mPdactivityNameEditText;
    private CheckBox mPdactivityNotifyCheckBox;
    private long mPdroutineId;
    private long mPdactivityId;
    private long mStartTimeInMillis;
    private NumberPicker numberPicker;
    private boolean recommendLoaded;
    private int notifyTime;
    public AddPdactivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mPdroutineId = getActivity().getIntent().getLongExtra(Pdroutine.ID_STRING, 0);
        mPdactivityId = getActivity().getIntent().getLongExtra(Pdactivity.ID_STRING, 0);
        mEdit = getActivity().getIntent().getBooleanExtra(Pdroutine.EDIT, false);
        mAdd = getActivity().getIntent().getBooleanExtra(Pdroutine.ADD, false);

        if (mEdit) ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(R.string.edit_pdactivity_activity_title);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_accept) {

            String notifyMins = mNotifyTimeButton.getText().toString();


            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            mStartTimeInMillis = sharedPref.getLong("timeinmillis",0);
            //Toast.makeText(getActivity(),notifyMins.substring(0,notifyMins.indexOf(" ")),Toast.LENGTH_LONG).show();

            if (mPdactivityNameEditText.getText().toString().trim().equals("")) {
                mPdactivityNameEditText.setError("You must enter Activity Name");
                return true;
            }

            if(mPdactivityNotifyCheckBox.isChecked()){
                notifyTime = (int) Integer.parseInt(notifyMins.substring(0,notifyMins.indexOf(" ")));
            }
            else{
                notifyTime = -1;
            }

            if (mAdd) {
                if (mStartTimeButton.getText().toString().equals(getResources().getString(R.string.add_pdactivity_start_time_button))) {
                    mStartTimeButton.setError("You must choose Start Time");
                    return true;
                }

                if (mEndTimeButton.getText().toString().equals(getResources().getString(R.string.add_pdactivity_end_time_button))) {
                    mEndTimeButton.setText("");
                    //mEndTimeButton.setError("You must choose End Time");
                    //return true;
                }

                //Insert values
                Pdactivity pdactivity = new Pdactivity();
                pdactivity.add(getActivity(), mPdactivityNameEditText.getText().toString(), mStartTimeButton.getText().toString(),
                        mEndTimeButton.getText().toString(), mPdroutineId, notifyTime);

            }
            if (mEdit) {
                Pdactivity pdactivity = new Pdactivity();
                pdactivity.edit(getActivity(), mPdactivityId, mPdactivityNameEditText.getText().toString(),
                        mStartTimeButton.getText().toString(), mEndTimeButton.getText().toString(),notifyTime);
            }
        if(mPdactivityNotifyCheckBox.isChecked()) {
            String title = mPdactivityNameEditText.getText().toString();
            String text = "The activity will start at " + mStartTimeButton.getText().toString();
            Prodelp.setNotification(getActivity(), mStartTimeInMillis, notifyTime, title, text);
        }
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pdactivity_add_pdactivity_fragment, container, false);

        mNotifyTimeButton = (Button) rootView.findViewById(R.id.notify_time_button);
        mNotifyTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                numberPicker = new NumberPicker(getActivity());
                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(60);
                numberPicker.setFormatter(new NumberPicker.Formatter() {
                    @Override
                    public String format(int i) {
                        return String.format("%02d", i);
                    }
                });

                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(numberPicker.getValue()<2){
                            mNotifyTimeButton.setText(numberPicker.getValue()+ " minute");
                        } else {
                            mNotifyTimeButton.setText(numberPicker.getValue()+ " minutes");
                        }

                        //Toast.makeText(getActivity(), numberPicker.getValue() + "", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setView(numberPicker);
                builder.show();
            }
        });


        mPdactivityNameEditText = (EditText) rootView.findViewById(R.id.pdactivity_name_edittext);
        mStartTimeButton = (Button) rootView.findViewById(R.id.pdactivity_start_time_button);
        mEndTimeButton = (Button) rootView.findViewById(R.id.pdactivity_end_time_button);

        //((AddPdactivityActivity) getActivity()).setAllViews(pdactivityName, mStartTimeButton, mEndTimeButton);

        mPdactivityNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    if(!mPdactivityNameEditText.getText().toString().equals("")){
                        recommendLoaded = true;
                        RelativeLayout relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.add_pdactivity_fragment);
                        FetchTask fetchTask = new FetchTask(getActivity(),relativeLayout,relativeLayout.findViewById(R.id.notify_checkbox).getId());
                        fetchTask.execute("how to "+mPdactivityNameEditText.getText());
                    }
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow( mPdactivityNameEditText.getWindowToken(), 0);
                }
                return true;
            }
        });

        mStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!recommendLoaded && !mPdactivityNameEditText.getText().toString().equals("")){

                    RelativeLayout relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.add_pdactivity_fragment);
                    FetchTask fetchTask = new FetchTask(getActivity(),relativeLayout,relativeLayout.findViewById(R.id.notify_checkbox).getId());
                    fetchTask.execute("how to "+mPdactivityNameEditText.getText());

                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow( mPdactivityNameEditText.getWindowToken(), 0);
                }
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "startTimePicker");
            }
        });

        mEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "endTimePicker");
            }
        });

        mPdactivityNotifyCheckBox = (CheckBox) rootView.findViewById(R.id.notify_checkbox);
        mPdactivityNotifyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true)
                    mNotifyTimeButton.setEnabled(true);
                else
                    mNotifyTimeButton.setEnabled(false);
            }
        });

        if (mEdit) {
            Pdactivity pdactivity = new Pdactivity();
            Cursor cursor = pdactivity.getCursorById(getActivity(), mPdactivityId);
            mPdactivityNameEditText.setText(cursor.getString(ProdelpContract.PdroutineEntry.NAME_COL_INDEX));
            mStartTimeButton.setText(cursor.getString(ProdelpContract.PdroutineEntry.START_DATE_COL_INDEX));
            mEndTimeButton.setText(cursor.getString(ProdelpContract.PdroutineEntry.END_DATE_COL_INDEX));
            int notifyTime= cursor.getInt(ProdelpContract.PdactivityEntry.NOTIFY_COL_INDEX);
            if(notifyTime!=-1) {
                mPdactivityNotifyCheckBox.setChecked(true);
                mNotifyTimeButton.setText(notifyTime + "  minutes");
            }
        }

        if (savedInstanceState != null) {
            // Restore values from Instance
            mStartTimeButton.setText(savedInstanceState.getCharSequence("startButton"));
            mEndTimeButton.setText(savedInstanceState.getCharSequence("endButton"));
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence("startButton", mStartTimeButton.getText());
        outState.putCharSequence("endButton", mEndTimeButton.getText());
        super.onSaveInstanceState(outState);
    }
}

