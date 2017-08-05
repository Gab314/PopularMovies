package com.example.gabriel.popularmovies;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


@SuppressWarnings("ALL")
public class MovieFragment extends Fragment implements RecyclerMovieAdapter.ItemClickListener {
    private RecyclerMovieAdapter adapter;
    private Integer mItem;
    final String LOG_TAG = MovieFragment.class.getSimpleName();
    public MovieFragment() {

    }
    ArrayList<MovieItem> movieList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mItem = 0;
        if (savedInstanceState != null  && savedInstanceState.getSerializable("ItemMenuNr") != null){
            mItem = (Integer) savedInstanceState.getSerializable("ItemMenuNr");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.my_recycler_view, container, false);
            movieList = new ArrayList<>();
        RecyclerView rvMovies = (RecyclerView) rootView.findViewById(R.id.my_Recycler_View);
        RecyclerMovieAdapter adapter = new RecyclerMovieAdapter(getActivity(),movieList);
        //Parcelable state = movieListView.onSaveInstanceState();  Saving position in a movielist;
        //movieListView.onRestoreInstanceState(state);
        int numberOfColumns = 2;
            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
            rvMovies.setLayoutManager(mLayoutManager);
            rvMovies.setAdapter(adapter);
        adapter.setClickListener(this);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_sort_type, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id =item.getItemId();
        if (id == R.id.most_popular){
            updateMovies();
        }
        if (id==R.id.most_rated){
            updateMovies2();
        }
        if (id==R.id.favorite){
            updateMovies3();
        }
        return true;
    }
    public void updateMovies2() {
        FetchMovieTask movieTask = new FetchMovieTask();
        String hrated = "top_rated";
        mItem = 1;
        if (isOnline(getActivity())) {
            movieTask.execute(hrated);
        }
    }
    public void updateMovies() {
        FetchMovieTask movieTask = new FetchMovieTask();
        String pop = "popular";
        mItem = 0;
        if (isOnline(getActivity())) {
            movieTask.execute(pop);
        }
    }
    public void updateMovies3() {

        FavoriteFragment newFragment = new FavoriteFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, newFragment).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        state.putSerializable("ItemMenuNr",mItem);
    }

    public void onStart(){
        super.onStart();
        if (mItem == 1){
            updateMovies2();
        }else  updateMovies();
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onItemClick(View view, int position) {
        String[] movieArray = new String[6];
        try {
            movieArray[0] = adapter.getItem(position).getMovieId();
            movieArray[1] = adapter.getItem(position).getMovieDate();
            movieArray[2] = adapter.getItem(position).getMoviePoster();
            movieArray[3] = adapter.getItem(position).getMovieSynopsis();
            movieArray[4] = adapter.getItem(position).getMovieVote();
            movieArray[5] = adapter.getItem(position).getMovieTitle();
            Intent detailIntent = new Intent(getActivity(), DetailActivity.class)
                    .putExtra(Intent.EXTRA_TEXT, movieArray);
            startActivity(detailIntent);
        }catch (NullPointerException e){
            Log.e(LOG_TAG,"Error",e);
        }
    }


    private class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieItem>> {
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
        private ArrayList<MovieItem> getMovieDataFromJson(String movieJsonStr)
                throws JSONException{
            final String MDB_results = "results";
            final String MDB_Poster = "poster_path";
            final String MDB_ID = "id";
            final String MDB_synopsis = "overview";
            final String MDB_date = "release_date";
            final String MDB_Title = "original_title";
            final String MDB_vote = "vote_average";
            movieList = new ArrayList<>();

            try {
                JSONObject movieJson = new JSONObject(movieJsonStr);
                JSONArray movieArray = movieJson.getJSONArray(MDB_results);

                for (int i = 0; i < movieArray.length(); i++) {
                    String poster;
                    String id;
                    String synopsis;
                    String date;
                    String title;
                    String vote;
                    JSONObject popularResults = movieArray.getJSONObject(i);
                    poster = popularResults.getString(MDB_Poster);
                    id = popularResults.getString(MDB_ID);
                    synopsis = popularResults.getString(MDB_synopsis);
                    date = popularResults.getString(MDB_date);
                    title = popularResults.getString(MDB_Title);
                    vote = popularResults.getString(MDB_vote);
                    MovieItem movieItem = new MovieItem(id, poster, synopsis, date, title, vote);
                    movieList.add(movieItem);
                }
            }catch (Exception e){
                Log.e(LOG_TAG,"Error",e);
            }
            return movieList;

        }





        @Override
        protected ArrayList<MovieItem> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            try{
                final String MDB_BASE_URL = "https://api.themoviedb.org/3/movie";
                final String MDB_API_PARAMS = "api_key"; //nao sei se precisa do =
                final String MDB_API_KEY = BuildConfig.MOVIE_DB_API_KEY;
                Uri builtUri = Uri.parse(MDB_BASE_URL).buildUpon()
                        .appendPath( params[0])
                        .appendQueryParameter(MDB_API_PARAMS,MDB_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(1000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                StringBuilder buffer = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                if (reader == null){
                    return null;
                }
                String line;
                while ((line = reader.readLine()) !=null){
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return  null;
                }
                movieJsonStr = buffer.toString();

            }  catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try{
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG,"Error closing stream", e);
                    }
                }
            }
            try{
                return getMovieDataFromJson(movieJsonStr);
            }catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;

        }
        @Override
        protected void onPostExecute(ArrayList<MovieItem> movies){
                try {
                    if (adapter != null)
                    adapter.clear();
                }catch (NullPointerException e){
                    Log.e(LOG_TAG,"Error",e);
                }


            if (movies !=null){
                try{
               movieList.addAll(movies);
                adapter.notifyDataSetChanged();

            }catch (NullPointerException e) {
                Log.e(LOG_TAG,"Error",e);}
            }
        }
    }

}
