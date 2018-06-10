package com.samaras.muvi.Backend.Models;

import android.graphics.Bitmap;

import java.net.IDN;

public class Movie {
    private String title;
    private String description;
    private String genres;
    private String rating;
    private String  imageUrl;

    @Override
    public String toString() {
        return "Movie{" +
                " title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", genres='" + genres + '\'' +
                ", rating='" + rating + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public Movie() {

    }

    public Movie(String  title, String description, String genres, String rating, String imageUrl) {
        this.setDescription(description);
        this.setGenres(genres);
        this.setRating(rating);
        this.setImageUrl(imageUrl);
        this.setTitle(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenres() {
        return genres;
    }

    public String getRating() {
        return rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
