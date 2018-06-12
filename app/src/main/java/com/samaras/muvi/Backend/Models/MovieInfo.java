package com.samaras.muvi.Backend.Models;

import android.graphics.Bitmap;

import java.net.IDN;


public class MovieInfo {
    public String id;
    public String title;
    public String description;
    public String shortDescription;
    public String genres;
    public String rating;
    public String photoUrl;

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

    public MovieInfo(String id, String title, String description, String shortDescription, String genres, String rating, String photoUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.shortDescription = shortDescription;
        this.genres = genres;
        this.rating = rating;
        this.photoUrl = photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }


    public void printMovie() {
        System.out.println("------------------------");
        System.out.println("id: " + id);
        System.out.println("title: " + title);
        System.out.println("description: " + description);
        System.out.println("rating: " + rating);
    }


}
