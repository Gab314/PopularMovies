package com.example.gabriel.popularmovies;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;

import com.example.gabriel.popularmovies.R;
import com.example.gabriel.popularmovies.Sync.MovieDBSyncAdapter;
import com.example.gabriel.popularmovies.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.R.attr.fragment;

public class MovieFragment extends Fragment {
    public static ImageAdapter mImageAdapter;
    public static CursorAdapter mCursorAdapter;

    public MovieFragment() {

    }

    static ArrayList<MovieItem> movieList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_list, container, false);
        movieList = new ArrayList<>();
        ListView movieListView = (ListView) rootView.findViewById(R.id.list_listView);
        mImageAdapter = new ImageAdapter(getActivity(), movieList);
        movieListView.setAdapter(mImageAdapter);


        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String[] movieArray = new String[6];
                movieArray[0] = mImageAdapter.getItem(position).getMovieId();
                movieArray[1] = mImageAdapter.getItem(position).getMovieDate();
                movieArray[2] = mImageAdapter.getItem(position).getMoviePoster();
                movieArray[3] = mImageAdapter.getItem(position).getMovieSynopsis();
                movieArray[4] = mImageAdapter.getItem(position).getMovieVote();
                movieArray[5] = mImageAdapter.getItem(position).getMovieTitle();
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, movieArray);
                startActivity(detailIntent);
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sort_type, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.most_popular) {
            updateMovies();
        }
        if (id == R.id.most_rated) {
            updateMovies2();
        }
        if (id == R.id.favorites) {
            FetchFavoriteMovie fetchFavoriteMovie = new FetchFavoriteMovie();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, fetchFavoriteMovie);
            fragmentTransaction.commit();
        }
        return true;
    }

    public void updateMovies2() {
        String hrated = "top_rated";
        MovieDBSyncAdapter.syncImmediately(getActivity(), hrated);
    }

    public void updateMovies() {
        String popular = "most_popular";
        MovieDBSyncAdapter.syncImmediately(getActivity(), popular);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isOnline(getActivity())) {
            updateMovies();
        }
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}

