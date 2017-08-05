package com.example.gabriel.popularmovies.Sync;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.gabriel.popularmovies.BuildConfig;
import com.example.gabriel.popularmovies.DetailActivity;
import com.example.gabriel.popularmovies.ReviewItem;
import com.example.gabriel.popularmovies.data.MovieContract;

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

public class SyncWithDB extends AsyncTask<String,Void,String> {
    private Context mContext;

    public SyncWithDB(Context context){
        mContext = context;
    }

    private String getMovieDetailsDataFromJson(String movieJSonStr)

            throws JSONException {
        final String MDB_KEY = "key";
        final String MDB_RESULTS = "results";


        JSONObject movieJson = new JSONObject(movieJSonStr);
        JSONArray movieArray = movieJson.getJSONArray(MDB_RESULTS);

            JSONObject results = movieArray.getJSONObject(0);
            String resultString;
            resultString = results.getString(MDB_KEY);

            return resultString;

    }
    @Override
    protected String doInBackground(String... params) {

        if (params[0] == null) {
            return null;
        }
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonStr = null;

        String LOG_TAG = "SyncWithDB";
        try{
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
            if (inputStream == null){
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
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
            return getMovieDetailsDataFromJson(movieJsonStr);
        }catch (JSONException e){
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;

    }
    @Override
    protected void  onPostExecute(String result){
        String url = "https://www.youtube.com/watch?v=";
        Uri webpage = Uri.parse(url + result);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(intent);
        }
    }
}
