package com.samaras.muvi.Backend.Models;

import android.net.Uri;

public class UserObj {

    private String email;
    private String name;
    private Uri image;

    public UserObj(String email, String name, Uri image) {
        this.email = email;
        this.name = name;
        this.image = image;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Uri getImage() {
        return image;
    }
}