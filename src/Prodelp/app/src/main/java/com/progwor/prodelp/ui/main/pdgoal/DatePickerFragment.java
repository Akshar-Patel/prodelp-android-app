package com.progwor.prodelp.ui.main.pdgoal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import com.progwor.prodelp.R;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    CharSequence date;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current startDate as the default startDate in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int y, int monthOfYear, int dayOfMonth) {
        String day = String.format("%02d", dayOfMonth);
        String month = String.format("%02d", monthOfYear + 1);
        String year = Integer.toString(y);
        date = day + "/" + month + "/" + year;

            Button dueButton = (Button) getActivity().findViewById(R.id.pdgoal_due_date_button);
            dueButton.setText(date);
            dueButton.setError(null);
            //((AddPdroutineActivity) getActivity()).setStartDate(date);
    }

}
