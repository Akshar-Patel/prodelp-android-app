package com.progwor.prodelp.ui.main.pdroutine.pdactivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.progwor.prodelp.R;
import com.progwor.prodelp.core.Pdactivity;
import com.progwor.prodelp.core.Prodelp;
import com.progwor.prodelp.data.ProdelpContract;
import com.progwor.prodelp.data.ProdelpProvider;

public class PdactivityListAdapter extends CursorAdapter {

    private Context mContext;

    public PdactivityListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdactivity_listview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        final long id = cursor.getLong(ProdelpContract.PdactivityEntry.ID_COL_INDEX);
        String name = cursor.getString(ProdelpContract.PdactivityEntry.NAME_COL_INDEX);
        String startTime = cursor.getString(ProdelpContract.PdactivityEntry.START_TIME_COL_INDEX);
        String endTime = cursor.getString(ProdelpContract.PdactivityEntry.END_TIME_COL_INDEX);
        viewHolder.nameTextView.setText(name);
        if(endTime.equals(""))viewHolder.dateTextView.setText(startTime);
        else viewHolder.dateTextView.setText(startTime + " - " + endTime);
        viewHolder.overflowMenuImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.list_overflow_menu, popup.getMenu());
                popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu popupMenu) {
                        //viewHolder.overflowMenuImageButton.setBackgroundColor();
                    }
                });
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_edit:
                                //Toast.makeText(context, "Edited", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(mContext, AddPdactivityActivity.class);
                                intent.putExtra(Prodelp.EDIT, true);
                                intent.putExtra(Pdactivity.ID_STRING, id);
                                mContext.startActivity(intent);
                                return true;
                            case R.id.action_delete:
                                //Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                Pdactivity pdactivity = new Pdactivity();
                                pdactivity.delete(mContext, id);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });
    }

    private static class ViewHolder {

        private final TextView nameTextView;
        private final TextView dateTextView;
        private final ImageButton overflowMenuImageButton;

        public ViewHolder(View view) {
            nameTextView = (TextView) view.findViewById(R.id.pdactivity_listview_item_name_textview);
            dateTextView = (TextView) view.findViewById(R.id.pdactivity_listview_item_time_textview);
            overflowMenuImageButton = (ImageButton) view.findViewById(R.id.pdactivity_overflow_menu_imagebutton);
        }
    }
}
