package com.example.gabriel.popularmovies;

public class MovieItem {
    String movieId;
    String moviePoster;
    String movieSynopsis;
    String movieDate;
    public MovieItem(String id, String posterPath, String synopsis, String date) {
        moviePoster = posterPath;
        movieId = id;
        movieSynopsis = synopsis;
        movieDate = date;
    }

    public String getMoviePoster(){
        return moviePoster;
    }

    public String getMovieId(){
        return movieId;
    }

    public String getMovieSynopsis(){
        return movieSynopsis;
    }

    public  String getMovieDate(){
        return movieDate;
    }
}
