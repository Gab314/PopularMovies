package com.example.gabriel.popularmovies;

public class MovieItem {
    String movieId;
    String posterPath;

    public MovieItem(String id, String posterPath) {
        this.posterPath = posterPath;
        this.movieId = id;
    }
}
