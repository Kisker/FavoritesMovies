package com.ftninformatika.favoritesmovies.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftninformatika.favoritesmovies.R;
import com.ftninformatika.favoritesmovies.model.NavigationItem;

import java.util.List;

public class DrawerListViewAdapter extends BaseAdapter {

    private List<NavigationItem> items = null;
    private Activity activity;

    public DrawerListViewAdapter(List<NavigationItem> items, Activity activity) {
        this.items = items;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public NavigationItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.drawer_single_list, null);
        }

        ImageView imageIcon = convertView.findViewById(R.id.imageView_Icon);
        TextView tvTitle = convertView.findViewById(R.id.textView_Title_Drawer);
        TextView tvSubTitle = convertView.findViewById(R.id.textView_Subtitle_Drawer);

        tvTitle.setText(items.get(position).getTitle());
        tvSubTitle.setText(items.get(position).getSubtitle());

        imageIcon.setImageResource(items.get(position).getIcon());

        return convertView;
    }
}

