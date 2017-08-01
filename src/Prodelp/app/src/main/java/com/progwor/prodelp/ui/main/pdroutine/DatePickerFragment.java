package com.progwor.prodelp.ui.main.pdroutine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.progwor.prodelp.R;
import com.progwor.prodelp.core.Prodelp;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    CharSequence date;
    int calledTimes=0;

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

        Button startButton = (Button) getActivity().findViewById(R.id.pdroutine_start_date_button);
        Button endButton = (Button) getActivity().findViewById(R.id.pdroutine_end_date_button);

        if (getTag().equals("startDatePicker")) {

            startButton.setText(date);
            startButton.setError(null);
            //((AddPdroutineActivity) getActivity()).setStartDate(date);

        }
        if (getTag().equals("endDatePicker")) {
            if (calledTimes == 0) {
                String startDateString = startButton.getText().toString();
                startDateString = startDateString.substring(6) + startDateString.substring(3, 5) + startDateString.substring(0, 2);
                long startDate = Long.parseLong(startDateString);

                String endDateString = year + month + day;
                long endDate = Long.parseLong(endDateString);

                if (endDate < startDate) {
                    Prodelp.showDialog(getActivity(),"End Date must be greater than Start Date.");
                } else {
                    endButton.setText(date);
                    endButton.setError(null);
                }
            }
        }
        calledTimes++;
    }
}
