package com.ftninformatika.favoritesmovies.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftninformatika.favoritesmovies.R;
import com.ftninformatika.favoritesmovies.model.Search;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchListViewAdapter extends BaseAdapter {

    private List<Search> movies = null;
    private Activity activity;

    public SearchListViewAdapter(Activity activity, List<Search> movies) {
        this.movies = movies;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Search getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.listview_search, null);
        }

        ImageView imageMovie = convertView.findViewById(R.id.image_Movie);
        TextView tvTitle = convertView.findViewById(R.id.textView_Title);
        TextView tvYear = convertView.findViewById(R.id.textView_Year);
        TextView tvType = convertView.findViewById(R.id.textView_Plot);

        tvTitle.setText(movies.get(position).getTitle());
        tvYear.setText(movies.get(position).getYear());
        tvType.setText(movies.get(position).getType());
        Picasso.get().load(movies.get(position).getPoster()).into(imageMovie);

        return convertView;
    }
}

