package com.example.gabriel.popularmovies.Sync;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.gabriel.popularmovies.BuildConfig;
import com.example.gabriel.popularmovies.R;
import com.example.gabriel.popularmovies.ReviewAdapter;
import com.example.gabriel.popularmovies.ReviewItem;

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

public class SyncTrailerDB extends AppCompatActivity {


    public SyncTrailerDB() {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            SyncTrailerDB.SyncFragment2 fragment = new SyncFragment2();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, fragment);
            fragmentTransaction.commit();
        }
    }

    public static class SyncFragment2 extends Fragment {
        private ReviewAdapter nReviewAdapter;
        private String nStr;
        ArrayList<ReviewItem> nreviewList;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances){

            final View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
            nreviewList = new ArrayList<>();
            ListView reviewListView = (ListView) rootView.findViewById(R.id.review_List_view);
            Parcelable state = reviewListView.onSaveInstanceState();
            reviewListView.onRestoreInstanceState(state);
            Intent intent = getActivity().getIntent();
            nStr = intent.getStringExtra(Intent.EXTRA_TEXT);

            nReviewAdapter = new ReviewAdapter(getActivity(), nreviewList,"trailer");
            reviewListView.setAdapter(nReviewAdapter);
            reviewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    String result;
                    result = nReviewAdapter.getItem(position).getReviewContent();
                    String url = "https://www.youtube.com/watch?v=";
                    Uri webpage = Uri.parse(url + result);
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        getActivity().startActivity(intent);
                }
                }
            });
            return rootView;
        }
        @Override
        public void onStart() {
            super.onStart();
            updateReviews();
        }

        public void updateReviews() {
            FetchReviewsDB fetchReviewsDB = new FetchReviewsDB();
            fetchReviewsDB.execute(nStr);
        }

        private class FetchReviewsDB extends AsyncTask<String, Void, ArrayList<ReviewItem>> {


            private final String LOG_TAG = SyncReviewsDB.class.getSimpleName();

            private ArrayList<ReviewItem> getMovieDataFromJson(String movieJsonStr)
                    throws JSONException {
                final String MDB_results = "results";
                final String MDB_Author = "name";
                final String MDB_Content = "key";
                nreviewList = new ArrayList<>();

                JSONObject movieJson = new JSONObject(movieJsonStr);
                JSONArray movieArray = movieJson.getJSONArray(MDB_results);

                for (int i = 0; i < movieArray.length(); i++) {
                    String author;
                    String content;

                    JSONObject popularResults = movieArray.getJSONObject(i);
                    author = popularResults.getString(MDB_Author);
                    content = popularResults.getString(MDB_Content);

                    ReviewItem reviewItem = new ReviewItem(author,content);

                    nreviewList.add(reviewItem);

                }
                return nreviewList;

            }


            @Override
            protected ArrayList<ReviewItem> doInBackground(String... params) {
                if (params.length == 0) {
                    return null;
                }
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String movieJsonStr = null;

                try {
                    Uri.Builder urlBuilder = new Uri.Builder();
                    final String MDB_BASE_URL = "api.themoviedb.org";
                    final String MDB_NUMBER = "3";
                    final String MDB_MOVIES = "movie";
                    final String MDB_VIDEOS = "videos";
                    final String MDB_API_PARAMS = "api_key";
                    final String MDB_API_KEY = BuildConfig.MOVIE_DB_API_KEY;
                    urlBuilder.scheme("http");
                    urlBuilder.authority(MDB_BASE_URL);
                    urlBuilder.appendEncodedPath(MDB_NUMBER);
                    urlBuilder.appendEncodedPath(MDB_MOVIES);
                    urlBuilder.appendEncodedPath(params[0]);
                    urlBuilder.appendEncodedPath(MDB_VIDEOS);
                    urlBuilder.appendQueryParameter(MDB_API_PARAMS, MDB_API_KEY);
                    URL url = new URL(urlBuilder.build().toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuilder buffer = new StringBuilder();
                    if (inputStream == null) {
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line).append("\n");
                    }

                    if (buffer.length() == 0) {
                        return null;
                    }
                    movieJsonStr = buffer.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            Log.e(LOG_TAG, "Error closing stream", e);
                        }
                    }
                }
                try {
                    return getMovieDataFromJson(movieJsonStr);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }
                return null;

            }

            @Override
            protected void onPostExecute(ArrayList<ReviewItem> result) {

                nReviewAdapter.clear();



                if (result != null) {
                    try {
                        nReviewAdapter.addAll(result);

                    } catch (NullPointerException e) {
                        Log.e(LOG_TAG, "Error", e);
                    }
                }
            }
        }

    }
}