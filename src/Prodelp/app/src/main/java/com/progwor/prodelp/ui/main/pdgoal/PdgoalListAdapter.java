package com.progwor.prodelp.ui.main.pdgoal;

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
import com.progwor.prodelp.core.Pdgoal;
import com.progwor.prodelp.core.Prodelp;
import com.progwor.prodelp.data.ProdelpContract;

public class PdgoalListAdapter extends CursorAdapter {
    public PdgoalListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdgoal_listview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        final long id = cursor.getLong(ProdelpContract.PdgoalEntry.ID_COL_INDEX);
        String name = cursor.getString(ProdelpContract.PdgoalEntry.NAME_COL_INDEX);
        String dueDate = cursor.getString(ProdelpContract.PdgoalEntry.DUE_DATE_COL_INDEX);
        viewHolder.nameTextView.setText(name);
        viewHolder.dateTextView.setText(dueDate);
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
                                Intent intent = new Intent(context, AddPdgoalActivity.class);
                                intent.putExtra(Prodelp.EDIT, true);
                                intent.putExtra(Pdgoal.ID_STRING, id);
                                context.startActivity(intent);
                                return true;
                            case R.id.action_delete:
                                //Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                Pdgoal pdgoal = new Pdgoal();
                                pdgoal.delete(context, id);
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
            nameTextView = (TextView) view.findViewById(R.id.pdgoal_listview_item_name_textview);
            dateTextView = (TextView) view.findViewById(R.id.pdgoal_listview_item_date_textview);
            overflowMenuImageButton = (ImageButton) view.findViewById(R.id.pdgoal_overflow_menu_imagebutton);
        }
    }
}
