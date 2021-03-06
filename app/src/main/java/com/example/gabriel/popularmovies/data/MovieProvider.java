package com.example.gabriel.popularmovies.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

@SuppressWarnings("ALL")
public class MovieProvider extends ContentProvider {
    public static final int FAVORITEDB = 100;
    public static final int FAVORITEDB_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        //Directory for Favorite Movies DB
        matcher.addURI(authority,MovieContract.PATH_FAVORITEDB, FAVORITEDB);
        //single item
        matcher.addURI(authority, MovieContract.PATH_FAVORITEDB + "/#", FAVORITEDB_WITH_ID);

        return matcher;
    }

    private MovieDBHelper mMovieDBHelper;

    @Override
    //Starts MovieDBHelper ( creates the tables)
    public boolean onCreate() {
        Context context = getContext();
        mMovieDBHelper = new MovieDBHelper(context);
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings1, String s1) {
        final SQLiteDatabase db = mMovieDBHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {

            case FAVORITEDB:{
                retCursor = db.query(MovieContract.FavoriteEntry.TABLE_NAME,
                        strings,
                        s,
                        strings1,
                        null,
                        null,
                        s1);
                break;
            }

            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return retCursor;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVORITEDB:
                return "vnd.android.cursor.dir" + "/" + MovieContract.CONTENT_AUTHORITY + "/" + MovieContract.PATH_FAVORITEDB;

            case FAVORITEDB_WITH_ID:
                return "vnd.android.cursor.item" + "/" + MovieContract.CONTENT_AUTHORITY + "/" + MovieContract.PATH_FAVORITEDB;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        final SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVORITEDB:{
                long id = db.insert(MovieContract.FavoriteEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.FavoriteEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {

        final SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int tasksDeleted;
        if ( null == s) s = "1";
        switch (match) {

            case FAVORITEDB_WITH_ID:{
                String id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(MovieContract.FavoriteEntry.TABLE_NAME, "movie_id=?", new String[]{id});
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksDeleted !=0) {getContext().getContentResolver().notifyChange(uri, null);}


        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowUpdated;
        String id = uri.getPathSegments().get(1);
        switch (match){
            case FAVORITEDB_WITH_ID:{
                rowUpdated = db.update(MovieContract.FavoriteEntry.TABLE_NAME, contentValues, "_id=?", new String[]{id});
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        if (rowUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowUpdated;
    }
}