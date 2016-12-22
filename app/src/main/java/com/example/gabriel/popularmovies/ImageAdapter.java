package com.example.gabriel.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ImageAdapter extends ArrayAdapter {

    public ImageAdapter(Context context, int resource, ItemMovie itemMovie) {
        super(context, resource);
    }

    public View addImage(ItemMovie itemMovie, View view, ViewGroup parent){

        Set set = itemMovie.entrySet();
        Iterator iterator = set.iterator();

        while (iterator.hasNext()){
            Map.Entry mEntry = (Map.Entry)iterator.next();
            mEntry.getValue();
            final String BASE_URL = "http://image.tmdb.org/t/p/w185";

            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.movie_grid, parent, false);
            }
            ImageView poster = (ImageView) view.findViewById(R.id.detail_poster_ImageView);

            Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(mEntry.getValue().toString()).build();

            try {
                URL url = new URL(uri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            Picasso.with(getContext()).setLoggingEnabled(true);
            Picasso.with(getContext()).load(uri).into(poster);
        }
        return view;
    }
}
