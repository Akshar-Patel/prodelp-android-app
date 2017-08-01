package com.progwor.prodelp.ui.main.pdreview;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.progwor.prodelp.R;
import com.progwor.prodelp.data.ProdelpContract;

public class PdreviewListAdapter extends CursorAdapter {

    private Context mContext;

    public PdreviewListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdreview_listview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        final String name = cursor.getString(ProdelpContract.PdactivityEntry.NAME_COL_INDEX);
        final String startTime = cursor.getString(ProdelpContract.PdactivityEntry.START_TIME_COL_INDEX);
        final String endTime = cursor.getString(ProdelpContract.PdactivityEntry.END_TIME_COL_INDEX);

        viewHolder.pdactivityCheckBox.setText(name);

        if(endTime.equals(""))viewHolder.pdactivityTextView.setText(startTime);
        else viewHolder.pdactivityTextView.setText(startTime + " - " + endTime);
    }

    private static class ViewHolder {

        private final CheckBox pdactivityCheckBox;
        private final TextView pdactivityTextView;

        public ViewHolder(View view) {
            pdactivityCheckBox = (CheckBox) view.findViewById(R.id.pdreview_listview_item_checkbox);
            pdactivityTextView = (TextView) view.findViewById(R.id.pdreview_listview_item_textview);
        }
    }

}
