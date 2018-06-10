package com.samaras.muvi.Backend.Models;

import android.graphics.Bitmap;

import java.net.IDN;


public class MovieInfo {
    public String id;
    public String title;
    public String description;
    public String genres;
    public String rating;
    public Bitmap bitmap;

    public boolean equals(Object o) {
        return (o instanceof MovieInfo) && (((MovieInfo)o).getTitle()).equals(this.title);
    }

    public int hashCode() {
        return title.hashCode();
    }


    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public MovieInfo() {
    }

    public MovieInfo(String id, String title, String description, String rating, String genres, Bitmap bitmap) {
        this.genres = genres;
        this.id = id;
        this.rating = rating;
        this.title = title;
        this.description = description;
        this.bitmap = bitmap;
    }


    public void printMovie() {
        System.out.println("------------------------");
        System.out.println("id: " + id);
        System.out.println("title: " + title);
        System.out.println("description: " + description);
        System.out.println("rating: " + rating);
    }


}
