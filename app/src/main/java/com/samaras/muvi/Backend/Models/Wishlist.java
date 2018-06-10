package com.samaras.muvi.Backend.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashSet;


public class Wishlist {
    private static Wishlist instance = null;
    public  HashSet<MovieInfo> list;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildListener;
    FirebaseUser user;


    protected Wishlist() {
        fillWatchlist();
    }

    public void fillWatchlist () {
        user = FirebaseAuth.getInstance().getCurrentUser();
        list = new HashSet<>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("watchlist");
        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Movie movie = dataSnapshot.getValue(Movie.class);
                MovieInfo movieInfo = new MovieInfo();

                movieInfo.id = dataSnapshot.getKey();
                movieInfo.description = movie.getDescription();
                movieInfo.genres = movie.getGenres();
                movieInfo.rating = movie.getRating();
                movieInfo.title = movie.getTitle();
                try {
                    java.net.URL photoURL = new java.net.URL(movie.getImageUrl());
                    HttpURLConnection connection = (HttpURLConnection) photoURL
                            .openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    movieInfo.bitmap = myBitmap;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                list.add(movieInfo);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildListener);
    }

    public static Wishlist getInstance() {
        if (instance == null)
            instance = new Wishlist();
        return instance;
    }

    public void addMovie(MovieInfo movieInfo) {
        list.add(movieInfo);
    }

    public void clearWishlist () {
        list.clear();
    }
    public boolean containsMovie (Movie movieToFind){
        for(MovieInfo movie : list)
            if(movie.title.equals(movieToFind.getTitle()) ){
                return  true;
            }

        return  false;
    }
    public int size() {
        return list.size();
    }




}
