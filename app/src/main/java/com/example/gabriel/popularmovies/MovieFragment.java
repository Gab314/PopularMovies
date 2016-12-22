package com.example.gabriel.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
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

public class MovieFragment extends Fragment {
    private ArrayAdapter<String> mMovieAdapter;

    public MovieFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_list, container, false);

        mMovieAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.movie_grid,
                R.id.grid_view_textView,
                new ArrayList<String>());


        ListView listView = (ListView) rootView.findViewById(R.id.list_listView);
        listView.setAdapter(mMovieAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String movie = mMovieAdapter.getItem(position);
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, movie);
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
        String hrated = "vote_average.desc";
        movieTask.execute(hrated);
    }
    public void updateMovies() {
        FetchMovieTask movieTask = new FetchMovieTask();
        String pop = "popularity.desc";
        movieTask.execute(pop);
    }
    @Override
    public void onStart(){
        super.onStart();
        updateMovies();
    }
    public class FetchMovieTask extends AsyncTask<String, Void, ItemMovie> {
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
        private ItemMovie getMovieDataFromJson(String movieJsonStr)
                throws JSONException{
            final String MDB_results = "results";
            final String MDB_Poster = "poster_path";
            final String MDB_ID = "id";
            int a =0;
            String b = "NOPE";
            ItemMovie itemMovie = new ItemMovie(a, b);
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(MDB_results);

            for(int i=0; i< movieArray.length(); i++){
                String poster;
                String id;
                JSONObject popularResults = movieArray.getJSONObject(i);
                poster = popularResults.getString(MDB_Poster);
                id = popularResults.getString(MDB_ID);
                itemMovie.put(id,poster);

            }
            return itemMovie;

        }





        @Override
        protected ItemMovie doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;
            Bitmap PosterIcon;
            try{
                final String MDB_BASE_URL = "https://api.themoviedb.org/3/discover/movie";
                final String MDB_API_PARAMS = "api_key"; //nao sei se precisa do =
                final String MDB_API_KEY = "8e445f0117d2e19e134382f9a2baf528";
                final String MDB_SORT_PARAM = "sort_by";
                Uri builtUri = Uri.parse(MDB_BASE_URL).buildUpon()
                        .appendQueryParameter(MDB_SORT_PARAM, params[0])
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
        protected void onPostExecute(ItemMovie result){
            if (result!=null){

            }
        }
    }

}
