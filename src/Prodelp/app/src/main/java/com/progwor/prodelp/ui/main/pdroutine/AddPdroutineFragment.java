package com.progwor.prodelp.ui.main.pdroutine;

import android.app.Service;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.progwor.prodelp.R;
import com.progwor.prodelp.core.Pdroutine;
import com.progwor.prodelp.data.ProdelpContract;
import com.progwor.prodelp.ui.main.FetchTask;

public class AddPdroutineFragment extends Fragment {

    boolean mEdit;
    boolean mAdd;
    private EditText mPdroutineNameEditText;
    private Button mStartDateButton, mEndDateButton;
    private long mPdroutineId;

    public AddPdroutineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPdroutineId = getActivity().getIntent().getLongExtra(Pdroutine.ID_STRING, 0);
        mAdd = getActivity().getIntent().getBooleanExtra(Pdroutine.ADD, false);
        mEdit = getActivity().getIntent().getBooleanExtra(Pdroutine.EDIT, false);
          }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mEdit) ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(R.string.edit_pdroutine_activity_title);

        RelativeLayout relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.add_pdroutine_fragment);
        FetchTask fetchTask = new FetchTask(getActivity(),relativeLayout,relativeLayout.findViewById(R.id.pdroutine_end_date_button).getId());
        fetchTask.execute("create daily routine");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pdroutine_add_pdroutine_fragment, container, false);
        mPdroutineNameEditText = (EditText) rootView.findViewById(R.id.pdroutine_name_edittext);
        mStartDateButton = (Button) rootView.findViewById(R.id.pdroutine_start_date_button);
        mEndDateButton = (Button) rootView.findViewById(R.id.pdroutine_end_date_button);

        mStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow( mPdroutineNameEditText.getWindowToken(), 0);

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "startDatePicker");
            }
        });

        mEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "endDatePicker");
            }
        });

        if (mEdit) {
            Pdroutine pdroutine = new Pdroutine();
            Cursor cursor = pdroutine.getCursorById(getActivity(), mPdroutineId);
            mPdroutineNameEditText.setText(cursor.getString(ProdelpContract.PdroutineEntry.NAME_COL_INDEX));
            mStartDateButton.setText(cursor.getString(ProdelpContract.PdroutineEntry.START_DATE_COL_INDEX));
            mEndDateButton.setText(cursor.getString(ProdelpContract.PdroutineEntry.END_DATE_COL_INDEX));
        }

        if (savedInstanceState != null) {
            // Restore values from Instance
            mStartDateButton.setText(savedInstanceState.getCharSequence("startButton"));
            mEndDateButton.setText(savedInstanceState.getCharSequence("endButton"));
        }
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_accept) {

            if (mPdroutineNameEditText.getText().toString().trim().equals("")) {
                mPdroutineNameEditText.setError("You must enter Routine Name");
                return true;
            }
            if (mAdd) {
                if (mStartDateButton.getText().toString().equals(getResources().getString(R.string.add_pdroutine_start_date_button))) {
                    mStartDateButton.setError("You must choose Start Date");
                    return true;
                }

                if (mEndDateButton.getText().toString().equals(getResources().getString(R.string.add_pdroutine_end_date_button))) {
                    mEndDateButton.setError("You must choose End Date");
                    return true;
                }

                //Add values
                Pdroutine pdroutine = new Pdroutine();
                pdroutine.add(getActivity(), mPdroutineNameEditText.getText().toString(), mStartDateButton.getText().toString(), mEndDateButton.getText().toString());
            }
            if (mEdit) {
                Pdroutine pdroutine = new Pdroutine();
                pdroutine.edit(getActivity(), mPdroutineId, mPdroutineNameEditText.getText().toString(), mStartDateButton.getText().toString(), mEndDateButton.getText().toString());
            }
            //mPdroutineName = pdroutineNameEditText.getText();




           /* //Insert values
            ContentValues routineValues = new ContentValues();
            routineValues.put(ProdelpContract.PdroutineEntry.NAME_COL, mPdroutineNameEditText.getText().toString());
            routineValues.put(ProdelpContract.PdroutineEntry.START_DATE_COL, mStartDateButton.getText().toString());
            routineValues.put(ProdelpContract.PdroutineEntry.END_DATE_COL, mEndDateButton.getText().toString());
            Uri routineUri = getActivity().getContentResolver().insert(ProdelpContract.PdroutineEntry.CONTENT_URI, routineValues);
            long routineRowId = ContentUris.parseId(routineUri);*/

            // startDate = fragment.getArguments().getCharSequence("startDate");
            //Toast.makeText(this, Long.toString(routineRowId), Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence("startButton", mStartDateButton.getText());
        outState.putCharSequence("endButton", mEndDateButton.getText());
        super.onSaveInstanceState(outState);
    }
}
