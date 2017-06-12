package com.example.gabriel.popularmovies;

public class MovieItem {
    String movieId;
    String moviePoster;
    String movieSynopsis;
    String movieDate;
    String movieTitle;
    String movieVote;
    public MovieItem(String id, String posterPath, String synopsis, String date, String title, String vote) {
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
    String getMovieVote(){
        return movieVote;

    }
}
