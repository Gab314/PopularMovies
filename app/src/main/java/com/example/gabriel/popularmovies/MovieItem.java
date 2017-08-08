package com.example.gabriel.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.ViewDebug;

class MovieItem implements Parcelable{
    private String movieId;
    private String moviePoster;
    private String movieSynopsis;
    private String movieDate;
    private String movieTitle;
    private String movieVote;

    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {

        @Override
        public MovieItem createFromParcel(Parcel source){
            return new MovieItem(source);
        }

        @Override
        public MovieItem[] newArray(int i) {
            return new MovieItem[i];
        }
    };

    MovieItem(String id, String posterPath, String synopsis, String date, String title, String vote) {
        moviePoster = posterPath;
        movieId = id;
        movieSynopsis = synopsis;
        movieDate = date;
        movieTitle = title;
        movieVote = vote;
    }
    String getMoviePoster(){
        return moviePoster;
    }

    String getMovieId(){
        return movieId;
    }

    String getMovieSynopsis(){
        return movieSynopsis;
    }

    String getMovieDate(){
        return movieDate;
    }

    String getMovieTitle(){
        return movieTitle;
    }
    String getMovieVote() {
        return movieVote;
    }

    @Override
    public int describeContents() {
        return 0;
    }
public MovieItem(Parcel in){
    moviePoster = in.readString();
    movieId = in.readString();
    movieSynopsis = in.readString();
    movieDate = in.readString();
    movieTitle = in.readString();
    movieVote = in.readString();
}
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(moviePoster);
        parcel.writeString(movieId);
        parcel.writeString(movieSynopsis);
        parcel.writeString(movieDate);
        parcel.writeString(movieTitle);
        parcel.writeString(movieVote);
    }
}