package com.example.gabriel.popularmovies;

import java.util.HashMap;

public class ItemMovie extends HashMap{
    int j;
    String poster;
    HashMap<Integer, String> mID = new HashMap<Integer, String>();

    public ItemMovie(Integer i, String posterURL){
            this.j = i;
            this.poster = posterURL;
    }
}
