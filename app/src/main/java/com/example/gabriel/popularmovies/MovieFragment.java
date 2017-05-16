package com.example.gabriel.popularmovies;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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

public class MovieFragment extends Fragment {
    private ImageAdapter mImageAdapter;
    public MovieFragment() {

    }
    ArrayList<MovieItem> movieList;

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
                        .putExtra(Intent.EXTRA_TEXT,movieArray);
                startActivity(detailIntent);
            }
        });
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
        return true;
    }
    public void updateMovies2() {
        FetchMovieTask movieTask = new FetchMovieTask();
        String hrated = "top_rated";
        movieTask.execute(hrated);
    }
    public void updateMovies() {
        FetchMovieTask movieTask = new FetchMovieTask();
        String pop = "popular";
        movieTask.execute(pop);
    }
    @Override
    public void onStart(){
        super.onStart();
        if(isOnline(getActivity())){
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
    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieItem>> {
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
        private ArrayList<MovieItem> getMovieDataFromJson(String movieJsonStr)
                throws JSONException{
            final String MDB_results = "results";
            final String MDB_Poster = "poster_path";
            final String MDB_ID = "id";
            final String MDB_synopsis = "overview";
            final String MDB_date = "release_date";
            final String MDB_title = "original_title";
            final String MDB_vote = "vote_average";
            movieList = new ArrayList<>();
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(MDB_results);

            for(int i=0; i< movieArray.length(); i++){
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
                title = popularResults.getString(MDB_title);
                vote = popularResults.getString(MDB_vote);
                MovieItem movieItem = new MovieItem(id, poster, synopsis, date, title, vote);
                movieList.add(movieItem);
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
            Bitmap PosterIcon;
            try{
                final String MDB_BASE_URL = "https://api.themoviedb.org/3/movie";
                final String MDB_API_PARAMS = "api_key"; //nao sei se precisa do =
                final String MDB_API_KEY = "8e445f0117d2e19e134382f9a2baf528";
                Uri builtUri = Uri.parse(MDB_BASE_URL).buildUpon()
                        .appendPath( params[0])
                        .appendQueryParameter(MDB_API_PARAMS,MDB_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null){
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) !=null){
                    buffer.append(line + "\n");
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
        //@Override
        protected void onPostExecute(ArrayList<MovieItem> movies){
            mImageAdapter.clear();
            if (movies !=null){
                try{
            mImageAdapter.addAll(movies);
            }catch (NullPointerException e) {
                Log.e(LOG_TAG,"Error",e);}
            }
        }
    }

}
