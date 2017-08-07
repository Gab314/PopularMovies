package com.example.gabriel.popularmovies;


public class ReviewItem {
    private String mAuthor;
    private String mContent;

    public ReviewItem(String author, String content) {
        mAuthor  = author;
        mContent = content;
    }

    String getReviewAuthor(){
        return mAuthor;
    }

   public String getReviewContent(){
        return mContent;
    }

}
