package com.example.gabriel.popularmovies;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.gabriel.popularmovies.data.MovieContract;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class FavoriteFragment extends MovieFragment implements  android.app.LoaderManager.LoaderCallbacks<Cursor> {

    private ImageAdapter nImageAdapter;
    private CursorAdapter nCursorAdapter;
    ArrayList<MovieItem> nmovieList;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;

    public FavoriteFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

            getLoaderManager().initLoader(TASK_LOADER_ID, null, FavoriteFragment.this);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_list, container, false);
        ListView movieListView = (ListView) rootView.findViewById(R.id.list_listView);
        nmovieList = new ArrayList<>();
        Cursor nCursor = null;
        nCursorAdapter = new CursorAdapter(getActivity(),nCursor, false);
        movieListView.setAdapter(nCursorAdapter);

        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String[] movieArray = new String[6];
                Cursor c = getActivity().getContentResolver().query(MovieContract.FavoriteEntry.CONTENT_URI,null,null,null,null);
                c.moveToPosition(position);
                movieArray[0] = c.getString(1);
                c.close();
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, movieArray);

                startActivity(detailIntent);
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_home, menu);

    }

    @Override
    public void onResume() {
        super.onResume();
        // re-queries for all tasks
        getLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id =item.getItemId();
        if (id == R.id.menu_home){
            MovieFragment newFragment = new MovieFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.container, newFragment).commit();
        }
        return true;
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Uri FavUri = MovieContract.FavoriteEntry.CONTENT_URI;
        //return new CursorLoader(getActivity(), FavUri, null, null, null, null);
        return new android.content.AsyncTaskLoader<Cursor>(getActivity()) {
            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            @Override
            protected void onStartLoading(){

                if (mTaskData != null){
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                }else{
                    forceLoad();
                }
            }


            public Cursor loadInBackground() {


                try {
                    return getActivity().getContentResolver().query(MovieContract.FavoriteEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                }catch (Exception e){
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data){
                mTaskData = data;
                super.deliverResult(data);
            }
        };


    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
            nCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
            nCursorAdapter.swapCursor(null);
    }


}
