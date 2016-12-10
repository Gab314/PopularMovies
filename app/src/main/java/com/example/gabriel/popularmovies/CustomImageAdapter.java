package com.example.gabriel.popularmovies;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CustomImageAdapter extends ArrayAdapter {


    public CustomImageAdapter(Context context, List<TmdbMovie> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final String BASE_URL = "http://image.tmdb.org/t/p/w185";
        TmdbMovie tmdbMovie = (TmdbMovie) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_grid, parent, false);
        }
        ImageView poster = (ImageView) convertView.findViewById(R.id.detail_poster_ImageView);

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(tmdbMovie.posterPath).build();

        try {
            URL url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        Picasso.with(getContext()).setLoggingEnabled(true);
        Picasso.with(getContext()).load(uri).into(poster);

        return convertView;
    }
}
