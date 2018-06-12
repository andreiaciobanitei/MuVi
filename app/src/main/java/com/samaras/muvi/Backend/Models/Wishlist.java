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
import java.util.ArrayList;
import java.util.HashSet;


public class Wishlist {
    private static Wishlist instance = null;
    public ArrayList<MovieInfo> list;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildListener;
    FirebaseUser user;


    protected Wishlist() {
        fillWatchlist();
    }

    public void fillWatchlist () {
        user = FirebaseAuth.getInstance().getCurrentUser();
        list = new ArrayList<>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("watchlist");
        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MovieInfo movie = dataSnapshot.getValue(MovieInfo.class);
                movie.id = dataSnapshot.getKey();
                list.add(movie);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                MovieInfo movie = dataSnapshot.getValue(MovieInfo.class);
                list.remove(movie);
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
    public boolean containsMovie (MovieInfo movieToFind){
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
