package com.samaras.muvi.Backend.Models;

public class CinemaMovieInfo {
    public String id;
    public String title;
    public String genres;
    public String rating;
    public String length;
    public String time;
    public String poster_path;
    public String description;


    public boolean equals(Object o) {
        return (o instanceof CinemaMovieInfo) && (((CinemaMovieInfo)o).getTitle()).equals(this.title);
    }

    public int hashCode() {
        return title.hashCode();
    }


    public CinemaMovieInfo() {

    }

    public CinemaMovieInfo(String id, String title, String genres, String rating, String length, String time, String poster_path, String description) {
        this.id = id;
        this.title = title;
        this.genres = genres;
        this.rating = rating;
        this.length = length;
        this.time = time;
        this.poster_path = poster_path;
        this.description = description;
    }
    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoster_path() {

        return poster_path;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getGenres() {
        return genres;
    }

    public String getRating() {
        return rating;
    }

    public String getLength() {
        return length;
    }

    public String getTime() {
        return time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
