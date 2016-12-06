package com.example.gabriel.popularmovies;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.Iterator;

public class DetailActivity extends AppCompatActivity {

    public DetailActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            DetailFragment fragment = new DetailFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public static class DetailFragment extends Fragment {
        private String mForeStr;
        private ArrayAdapter<String> mMovieAdapter;
        private final String LOG_TAG = DetailActivity.DetailFragment.class.getSimpleName();
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_detail, container, false);
            Intent intent = getActivity().getIntent();

                mForeStr = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (intent.hasExtra(Intent.EXTRA_TEXT)){
                    ((TextView) rootView.findViewById(R.id.detail_title_TextView)).setText(mForeStr);
                }

            mMovieAdapter = new ArrayAdapter<String>(
                    getActivity(),
                    R.layout.activity_detail,
                    R.id.detail_release_date_TextView,
                    new ArrayList<String>());
            return rootView;
        }
        @Override
        public void onStart(){
            super.onStart();
            updateMovieDetail();
        }
        public void updateMovieDetail() {
            FetchMovieTask movieTask = new FetchMovieTask();
            String pop = Intent.EXTRA_TEXT;
            movieTask.execute(pop);
        }
    public class FetchMovieTask extends AsyncTask<String,Void,String[]>{
        TextView DateTextView = (TextView) getActivity().findViewById(R.id.detail_release_date_TextView);
        private String[] getMovieDetailsDataFromJson(String movieJSonStr)

            throws JSONException{
            final String MDB_results = "results";
            final String MDB_Poster = "poster_path";
            final String MDB_Title = "original_title";
            final String MDB_release_date = "release_date";
            final String MDB_overview = "overview";

            JSONObject movieJson = new JSONObject(movieJSonStr);
            JSONArray movieArray = movieJson.getJSONArray(MDB_results);

            String[] resultString = new String[3];


                String title;
                String poster;
                String date;
                String synopsis;
                JSONObject popularResults = movieArray.getJSONObject(0);
                title = popularResults.getString(MDB_Title);
                poster = popularResults.getString(MDB_Poster);
                date = popularResults.getString(MDB_release_date);
                synopsis = popularResults.getString(MDB_overview);
                resultString[0] = title;
                resultString[1] = poster;
                resultString[2] = date;
                resultString[3] = synopsis;


            return resultString;
        }
        @Override
        protected String[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;
            Bitmap PosterIcon;
            try{
                final String MDB_BASE_URL = "https://api.themoviedb.org/3/search/movie?";
                final String MDB_API_PARAMS = "api_key"; //nao sei se precisa do =
                final String MDB_API_KEY = "8e445f0117d2e19e134382f9a2baf528";
                final String MDB_SORT_QUERY = "query";
                Uri builtUri = Uri.parse(MDB_BASE_URL).buildUpon()
                        .appendQueryParameter(MDB_API_PARAMS, MDB_API_KEY)
                        .appendQueryParameter(MDB_SORT_QUERY, params[0])
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
                return getMovieDetailsDataFromJson(movieJsonStr);
            }catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;

        }
        @Override
        protected void onPostExecute(String[] result){
            if (result!=null){
                DateTextView.setText(result[3]);

            }
        }
    }
    }
    }
