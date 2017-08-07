package com.example.gabriel.popularmovies;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewAdapter extends ArrayAdapter<ReviewItem>{
private final String mTrailer;

    public ReviewAdapter(Context context, ArrayList<ReviewItem> reviews, String Trailer) {
        super(context, 0, reviews);
        mTrailer = Trailer;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView,@NonNull ViewGroup parent) {
        // Get the data item for this position
        ReviewItem review = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_layout, parent, false);
        }
        // Lookup view for data population
        TextView reviewAuthor = (TextView) convertView.findViewById(R.id.text_view_text1);
        TextView reviewContent = (TextView) convertView.findViewById(R.id.text_view_text2);
        // Populate the data into the template view using the data object
        reviewAuthor.setText(review.getReviewAuthor());
        if (mTrailer == null) {
            reviewContent.setText(review.getReviewContent());
        }

        // Return the completed view to render on screen
        return convertView;
    }

}
