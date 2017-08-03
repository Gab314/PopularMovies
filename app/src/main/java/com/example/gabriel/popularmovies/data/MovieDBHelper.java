package com.example.gabriel.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "popular_movies.db";

    public MovieDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + MovieContract.FavoriteEntry.TABLE_NAME + " ( " +
                MovieContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.FavoriteEntry.COLUMN_ID + " TEXT NOT NULL, " +
                MovieContract.FavoriteEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                MovieContract.FavoriteEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MovieContract.FavoriteEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                MovieContract.FavoriteEntry.COLUMN_RESULTS + " TEXT NOT NULL, " +
                MovieContract.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL" +

                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}