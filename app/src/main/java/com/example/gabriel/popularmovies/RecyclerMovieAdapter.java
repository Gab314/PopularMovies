package com.example.gabriel.popularmovies;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class RecyclerMovieAdapter extends RecyclerView.Adapter<RecyclerMovieAdapter.MyViewHolder>{
    private ItemClickListener mClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
         private ImageView mImageView;


        public MyViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.grid_view_ImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    private List<MovieItem> mMovieItems;
    private Context mContext;

public RecyclerMovieAdapter(Context context, List<MovieItem> movieItems){
    mContext = context;
    mMovieItems = movieItems;
}

private Context getContext(){
    return mContext;
}
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View movieView = inflater.inflate(R.layout.movie_grid, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(movieView);
        return  viewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final String BASE_URL = "http://image.tmdb.org/t/p/w185";

        MovieItem currentMovie = mMovieItems.get(position);
        ImageView poster = holder.mImageView;

        assert currentMovie != null;
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(currentMovie.getMoviePoster()).build();



        Picasso.with(getContext()).setLoggingEnabled(true);
        Picasso.with(getContext()).load(uri).into(poster);

    }


    @Override
    public int getItemCount() {
        return mMovieItems.size();
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    public MovieItem getItem(int id) {
        return mMovieItems.get(id);
    }
    public void clear() {
        mMovieItems.clear(); //clear list
        notifyDataSetChanged(); //let your adapter know about the changes and reload view.
    }
}
