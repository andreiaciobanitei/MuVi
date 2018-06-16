package com.samaras.muvi.Backend.Models;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CinemaSchedules {

    public static ArrayList<CinemaMovieInfo> mondaySchedule;
    public static ArrayList<CinemaMovieInfo> tuesdaySchedule;
    public static  ArrayList<CinemaMovieInfo> wednesdaySchedule;
    public static ArrayList<CinemaMovieInfo> thursdaySchedule;
    public static ArrayList<CinemaMovieInfo> fridaySchedule;
    public static ArrayList<CinemaMovieInfo> saturdaySchedule;
    public static ArrayList<CinemaMovieInfo> sundaySchedule;

    private static FirebaseDatabase mFirebaseDatabase;
    private static DatabaseReference mondayDatabaseReference;
    private static DatabaseReference tuesdayDatabaseReference;
    private static DatabaseReference wednesdayDatabaseReference;
    private static DatabaseReference thursdayDatabaseReference;
    private static DatabaseReference fridayDatabaseReference;
    private static DatabaseReference saturdayDatabaseReference;
    private static DatabaseReference sundayDatabaseReference;


    public static void getSchedule (String cinemaName) {
        mondaySchedule = new ArrayList<>();
        tuesdaySchedule = new ArrayList<>();
        wednesdaySchedule = new ArrayList<>();
        thursdaySchedule = new ArrayList<>();
        fridaySchedule = new ArrayList<>();
        saturdaySchedule = new ArrayList<>();
        sundaySchedule = new ArrayList<>();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mondayDatabaseReference = mFirebaseDatabase.getReference().child("cinema_schedules").child(cinemaName).child("monday");
        mondayDatabaseReference.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            CinemaMovieInfo movie = dataSnapshot.getValue(CinemaMovieInfo.class);
            movie.id = dataSnapshot.getKey();
            mondaySchedule.add(movie);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) { ;
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }});
        tuesdayDatabaseReference = mFirebaseDatabase.getReference().child("cinema_schedules").child(cinemaName).child("tuesday");
        tuesdayDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CinemaMovieInfo movie = dataSnapshot.getValue(CinemaMovieInfo.class);
                movie.id = dataSnapshot.getKey();
                tuesdaySchedule.add(movie);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { ;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});
        wednesdayDatabaseReference = mFirebaseDatabase.getReference().child("cinema_schedules").child(cinemaName).child("wednesday");
        wednesdayDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CinemaMovieInfo movie = dataSnapshot.getValue(CinemaMovieInfo.class);
                movie.id = dataSnapshot.getKey();
                wednesdaySchedule.add(movie);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { ;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});
        thursdayDatabaseReference = mFirebaseDatabase.getReference().child("cinema_schedules").child(cinemaName).child("thursday");
        thursdayDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CinemaMovieInfo movie = dataSnapshot.getValue(CinemaMovieInfo.class);
                movie.id = dataSnapshot.getKey();
                thursdaySchedule.add(movie);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { ;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});
        fridayDatabaseReference = mFirebaseDatabase.getReference().child("cinema_schedules").child(cinemaName).child("friday");
        fridayDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CinemaMovieInfo movie = dataSnapshot.getValue(CinemaMovieInfo.class);
                movie.id = dataSnapshot.getKey();
                fridaySchedule.add(movie);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { ;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});
        saturdayDatabaseReference = mFirebaseDatabase.getReference().child("cinema_schedules").child(cinemaName).child("saturday");
        saturdayDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CinemaMovieInfo movie = dataSnapshot.getValue(CinemaMovieInfo.class);
                movie.id = dataSnapshot.getKey();
                saturdaySchedule.add(movie);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { ;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});
        sundayDatabaseReference = mFirebaseDatabase.getReference().child("cinema_schedules").child(cinemaName).child("sunday");
        sundayDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CinemaMovieInfo movie = dataSnapshot.getValue(CinemaMovieInfo.class);
                movie.id = dataSnapshot.getKey();
                sundaySchedule.add(movie);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { ;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});
    }
}
