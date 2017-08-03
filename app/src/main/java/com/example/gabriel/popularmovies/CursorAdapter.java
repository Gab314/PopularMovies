package com.example.gabriel.popularmovies;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

 class CursorAdapter extends android.widget.CursorAdapter{

    CursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(R.layout.movie_grid, viewGroup, false);

    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final String BASE_URL = "http://image.tmdb.org/t/p/w185";
        ImageView poster = (ImageView) view.findViewById(R.id.grid_view_ImageView);

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(cursor.getString(3)).build();

        try {
            URL url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Picasso.with(context).setLoggingEnabled(true);
        Picasso.with(context).load(uri).placeholder(R.mipmap.ic_launcher).into(poster);
    }
}
