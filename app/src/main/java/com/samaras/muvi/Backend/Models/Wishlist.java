package com.samaras.muvi.Backend.Models;

import java.util.HashSet;


public class Wishlist {
    private static Wishlist instance = null;
    public static HashSet<MovieInfo> list;

    protected Wishlist() {
        list = new HashSet<>();
    }

    public static Wishlist getInstance() {
        if (instance == null)
            instance = new Wishlist();
        return instance;
    }

    public void addMovie(MovieInfo movieInfo) {
        list.add(movieInfo);
    }

    public int size() {
        return list.size();
    }




}
