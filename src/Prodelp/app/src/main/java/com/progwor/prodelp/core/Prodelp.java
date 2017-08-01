package com.progwor.prodelp.core;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.progwor.prodelp.R;
import com.progwor.prodelp.ui.main.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Prodelp {

    public static final int LOADER = 0;

    public static final int PDROUTINE = 0;
    public static final int PDGOAL = 1;
    public static final int PDREVIEW = 2;
    public static final int PDPROGRESS = 3;


    public static final int PDACTIVITY = 4;


    //Strings to identify the operations
    public static final String ADD = "add";
    public static final String EDIT = "edit";

    public static void setUpToolbar(Context context, int title) {

        Toolbar toolbar = (Toolbar) ((ActionBarActivity) context).findViewById(R.id.common_toolbar);
        toolbar.setTitle(title);
        ((ActionBarActivity) context).setSupportActionBar(toolbar);
        ((ActionBarActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static void showDialog(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void setNotification(Context context, long startTime, int mins, String title, String text) {

        //String starTime;
        //int startMins = starTime

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("text", text);

        PendingIntent mAlarmSender = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(startTime);
        c.add(Calendar.MINUTE, -mins);
        long notifyTime = c.getTimeInMillis();

        AlarmManager am;
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, notifyTime,24 * 60 * 60 * 1000,mAlarmSender);
    }

    public static String convertTo24hours(String time12) {
        String time24 = null;
        Date date = null;
        try {
            date = new SimpleDateFormat("hh:mm aa").parse(time12);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String now = new SimpleDateFormat("hh:mm aa").format(date);
        SimpleDateFormat inFormat = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm");
        try {
            time24 = outFormat.format(inFormat.parse(now));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time24;
    }

    public static class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            NotificationManager notificationManager;
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // Set the icon, scrolling text and timestamp
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(intent.getStringExtra("title"))
                            .setContentText(intent.getStringExtra("text"))
                            .setSound(alarmSound);

            Intent resultIntent = new Intent(context, MainActivity.class);

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);
            //Notification notification = new Notification(R.drawable.ic_launcher, "Test Alarm",
            //       System.currentTimeMillis());
            // The PendingIntent to launch our activity if the user selects this notification
            //PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, AddPdroutineActivity.class), 0);
            // Set the info for the views that show in the notification panel.

            // notification.setLatestEventInfo(context, context.getText(R.string.add_pdactivity_end_time_button), "This is a Test Alarm", contentIntent);
            // Send the notification.
            // We use a layout id because it is a unique number. We use it later to cancel.
            //notificationManager.notify(R.string.add_pdactivity_activity_title, notification);

            // Sets an ID for the notification
            int mNotificationId = 001;

// Builds the notification and issues it.
            notificationManager.notify(mNotificationId, mBuilder.build());
        }
    }
}
