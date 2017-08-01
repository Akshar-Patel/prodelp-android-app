package com.progwor.prodelp.ui.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.progwor.prodelp.R;

public class DrawerAdapter extends ArrayAdapter<String> {

    Context context;
    int[] images;
    String[] texts;

    public DrawerAdapter(Context c, String[] t, int[] i) {

        super(c, R.layout.drawer_listview_item, R.id.drawer_item_textview, t);
        this.context = c;
        this.images = i;
        this.texts = t;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.drawer_listview_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.drawer_item_textview);
            viewHolder.imageView = (DrawerIconView) convertView.findViewById(R.id.drawer_item_imageview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(texts[position]);
        viewHolder.imageView.setImageResource(images[position]);
        return convertView;
    }

    public static class ViewHolder {
        TextView textView;
        DrawerIconView imageView;
    }
}