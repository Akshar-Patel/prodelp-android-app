package com.progwor.prodelp.ui.main.pdroutine;

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
import com.progwor.prodelp.core.Pdroutine;
import com.progwor.prodelp.data.ProdelpContract;

public class PdroutineListAdapter extends CursorAdapter {

    private Context mContext;

    public PdroutineListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdroutine_listview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        final long id = cursor.getLong(ProdelpContract.PdroutineEntry.ID_COL_INDEX);
        String name = cursor.getString(ProdelpContract.PdroutineEntry.NAME_COL_INDEX);
        String startDate = cursor.getString(ProdelpContract.PdroutineEntry.START_DATE_COL_INDEX);
        String endDate = cursor.getString(ProdelpContract.PdroutineEntry.END_DATE_COL_INDEX);
        viewHolder.nameTextView.setText(name);
        viewHolder.dateTextView.setText(startDate + " - " + endDate);
        viewHolder.overflowMenuImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.list_overflow_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_edit:
                                //Toast.makeText(context, "Edited", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(mContext, AddPdroutineActivity.class);
                                intent.putExtra(Pdroutine.EDIT, true);
                                intent.putExtra(Pdroutine.ID_STRING, id);
                                mContext.startActivity(intent);
                                return true;
                            case R.id.action_delete:
                                //Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                Pdroutine pdroutine = new Pdroutine();
                                pdroutine.delete(mContext, id);
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
            nameTextView = (TextView) view.findViewById(R.id.pdroutine_listview_item_name_textview);
            dateTextView = (TextView) view.findViewById(R.id.pdroutine_listview_item_date_textview);
            overflowMenuImageButton = (ImageButton) view.findViewById(R.id.pdroutine_overflow_menu_imagebutton);
        }
    }
}
