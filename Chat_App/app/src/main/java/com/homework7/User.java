package com.homework7;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shruti on 11/18/2016.
 */
public class User implements Serializable{
    private String fullName, email, uid, gender, photourl;
    private ArrayList<String> albumUrls = new ArrayList<>();

    public ArrayList<String> getAlbumUrls() {
        return albumUrls;
    }

    public void setAlbumUrls(ArrayList<String> albumUrls) {
        this.albumUrls = albumUrls;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
