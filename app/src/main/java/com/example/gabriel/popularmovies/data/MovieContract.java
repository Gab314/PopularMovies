package com.example.gabriel.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.gabriel.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIEDB = "MovieDB";
    public static final String PATH_FAVORITEDB = "FavoriteDB";
    public static final String PATH_TOP_RATEDDB = "Top_RatedDB";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIEDB).build();


        public static final String TABLE_NAME = "MovieDB";
        public static final String COLUMN_RESULTS = "results";
        public static final String COLUMN_POSTER = "poster_path";
        public static final String COLUMN_ID = "movie_id";
        public static final String COLUMN_SYNOPSIS = "overview";
        public static final String COLUMN_DATE = "release_date";
        public static final String COLUMN_TITLE = "original_title";
    }
    public static final class FavoriteEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITEDB).build();


        public static final String TABLE_NAME = "MovieDB";
        public static final String COLUMN_RESULTS = "results";
        public static final String COLUMN_POSTER = "poster_path";
        public static final String COLUMN_ID = "movie_id";
        public static final String COLUMN_SYNOPSIS = "overview";
        public static final String COLUMN_DATE = "release_date";
        public static final String COLUMN_TITLE = "original_title";
    }
    public static final class TopRatedEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TOP_RATEDDB).build();


        public static final String TABLE_NAME = "MovieDB";
        public static final String COLUMN_RESULTS = "results";
        public static final String COLUMN_POSTER = "poster_path";
        public static final String COLUMN_ID = "movie_id";
        public static final String COLUMN_SYNOPSIS = "overview";
        public static final String COLUMN_DATE = "release_date";
        public static final String COLUMN_TITLE = "original_title";
    }
}
