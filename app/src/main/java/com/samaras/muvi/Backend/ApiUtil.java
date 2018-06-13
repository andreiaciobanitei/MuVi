package com.samaras.muvi.Backend;

import com.samaras.muvi.Backend.Models.MovieInfo;
import com.samaras.muvi.Backend.Models.MovieList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApiUtil {
    private static final String API_KEY = "f76dfa1acb7d1a736694eec710bd040b";
    private static final String baseLink = "https://api.themoviedb.org/3";
    private static final String keyString = "?api_key=";
    private static MovieList movieList = new MovieList();


    public static String createURL(String requestString) {
        return baseLink + requestString + keyString + API_KEY;
    }

    public static String createPhotoURL(String pathString) {
        return "http://image.tmdb.org/t/p/w185//" + pathString.substring(1);
    }

    public static ArrayList<MovieInfo> getMoviesFromJson(String json) {
        ArrayList<MovieInfo> movies = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray arrayMovies = jsonObject.getJSONArray("results");

            for (int i = 0; i < arrayMovies.length(); i++) {
                JSONObject movieJson = arrayMovies.getJSONObject(i);

                int id = movieJson.getInt("id");
                String title = movieJson.getString("original_title");
                String description = movieJson.getString("overview");
                Double ratingDouble = movieJson.getDouble("vote_average");
                Double popularity = movieJson.getDouble("popularity");
                String path_to_jpg = movieJson.getString("poster_path");
                JSONArray genre_ids = movieJson.getJSONArray("genre_ids");
                String photoURLString = ApiUtil.createPhotoURL(path_to_jpg);
                String genres ="";
                String rating = "";
                String shortDescription;

                int description_length = description.length();
                int stop_index = 170;
                if(description_length >= 170)
                    for(int j = 150; j < 170; j++)
                        if(description.charAt(j) == ' ' || description.charAt(j) == '.')
                            stop_index = j;

                if(description_length > 170) {
                    shortDescription = description.substring(0, stop_index);
                    shortDescription = shortDescription + " [...]";
                } else {
                    shortDescription = description;
                }

                rating  = "Rating: " + Double.toString(ratingDouble);
                for (int j = 0; j < Math.min(genre_ids.length(), 3); j++) {
                    genres += movieList.getGenre(genre_ids.getInt(j));
                    if (j != Math.min(genre_ids.length(), 3) - 1)
                        genres += ", ";
                }

                MovieInfo movie = new MovieInfo(Integer.toString(id), title, description, shortDescription, genres, rating, photoURLString);
                movies.add(movie);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }

}
