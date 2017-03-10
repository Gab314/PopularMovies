package com.example.gabriel.popularmovies.data;

import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.gabriel.popularmovies";

    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "MovieDB";
        public static final String COLUMN_RESULTS = "results";
        public static final String COLUMN_POSTER = "poster_path";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_SYNOPSIS = "overview";
        public static final String COLUMN_DATE = "release_date";
    }
}
