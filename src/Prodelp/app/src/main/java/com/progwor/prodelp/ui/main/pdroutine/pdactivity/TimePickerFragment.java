package com.progwor.prodelp.ui.main.pdroutine.pdactivity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.progwor.prodelp.R;
import com.progwor.prodelp.core.Pdroutine;
import com.progwor.prodelp.core.Prodelp;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    CharSequence time;
    int calledTimes=0;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String ampm = "";

        int hour = hourOfDay;
        if (hour == 0) {
            hour = 12;
            ampm = "AM";
        } else if (hour == 12) {
            ampm = "PM";
        } else if (hour < 12) {
            ampm = "AM";
        } else if (hour > 12) {
            hour = hour - 12;
            ampm = "PM";
        }
        time = String.format("%02d", hour) + ":" + String.format("%02d", minute) + " " + ampm;
        //Toast.makeText(getActivity(), time + "", Toast.LENGTH_LONG).show();

        if (getTag().equals("startTimePicker")) {
            Button startButton = (Button) getActivity().findViewById(R.id.pdactivity_start_time_button);
            startButton.setText(time);
            startButton.setError(null);

            final Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY,hourOfDay);
            c.set(Calendar.MINUTE,minute);

            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong("timeinmillis",c.getTimeInMillis());
            editor.apply();

            //((AddPdactivityActivity) getActivity()).setStartTime(time);
        }
        if (getTag().equals("endTimePicker")) {
            if(calledTimes==0) {
                Button endButton = (Button) getActivity().findViewById(R.id.pdactivity_end_time_button);

                final Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                if (c.getTimeInMillis() <= sharedPref.getLong("timeinmillis", 0)) {
                    Prodelp.showDialog(getActivity(), "End Time must be greater than Start Time.");
                }

                endButton.setText(time);
                endButton.setError(null);
            }
        }
        calledTimes++;
    }
}
