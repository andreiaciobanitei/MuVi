package com.samaras.muvi.Backend.Models;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MemberData {
    private String name;
    private String color;
    private String photoUri;

    public MemberData(String name, String color, String photoUri) {
        this.name = name;
        this.color = color;
        this.photoUri = photoUri;
    }

    // Add an empty constructor so we can later parse JSON into MemberData using Jackson
    public MemberData() {
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getPhotoUri() {
        return photoUri;
    }
}