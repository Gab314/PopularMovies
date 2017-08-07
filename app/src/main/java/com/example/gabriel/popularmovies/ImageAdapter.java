package com.example.gabriel.popularmovies;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ImageAdapter extends ArrayAdapter<MovieItem> {

    public ImageAdapter(Context context, ArrayList<MovieItem> movies) {
        super(context, 0 , movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final String BASE_URL = "http://image.tmdb.org/t/p/w185";
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.my_recycler_view, parent, false);
        }
        MovieItem currentMovie = getItem(position);

        ImageView poster = (ImageView) listItemView.findViewById(R.id.recycler_View_rv);

        assert currentMovie != null;
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(currentMovie.getMoviePoster()).build();

        try {
            URL url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        Picasso.with(getContext()).setLoggingEnabled(true);
        Picasso.with(getContext()).load(uri).placeholder(R.mipmap.ic_launcher).into(poster);

        return listItemView;
    }


}

