package com.example.gabriel.popularmovies;

public class TmdbMovie {
    String movieName;
    String posterPath;

    public TmdbMovie(String movieName, String posterPath) {
        this.posterPath = posterPath;
        this.movieName = movieName;
    }
}
