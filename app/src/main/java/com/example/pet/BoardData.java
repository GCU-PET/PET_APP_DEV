package com.example.pet;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BoardData {
    @Expose
//    @SerializedName("title") private String title;
//    @SerializedName("content") private String content;
//    @SerializedName("userID") private String userID;

    @SerializedName("title") private String title;
    @SerializedName("date") private String date;
    @SerializedName("userID") private String userID;
    @SerializedName("content") private String content;
    @SerializedName("imageUri") private Uri imageUri;

    public String getTitle(){
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getUserID() {
        return  userID;
    }

    public String getContent() {
        return content;
    }

    public Uri getImageUri() {
        return imageUri;
    }

}
