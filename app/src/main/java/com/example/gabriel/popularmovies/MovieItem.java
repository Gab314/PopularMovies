package com.example.gabriel.popularmovies;

class MovieItem {
    private String movieId;
    private String moviePoster;
    private String movieSynopsis;
    private String movieDate;
    private String movieTitle;
    private String movieVote;
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
    String getMovieVote(){
        return movieVote;

    }
}
