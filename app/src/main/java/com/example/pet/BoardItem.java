package com.example.pet;

import android.net.Uri;


public class BoardItem {

    private String title;
    private String date;
    private String userID;
    private String content;
    private Uri imageUri;

//    (String title, String date, String userID, String content, Uri imageUri)

    public BoardItem(String title, String date, String userID, String content) {
        this.title = title;
        this.date = date;
        this.userID = userID;
        this.content = content;
        this.imageUri = imageUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) { this.content = content; }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

    public String getDate() { return date; }
    public void setDate(String date ) { this.date = date; }


//    public static BoardItem createWithoutImage(String title, String content) {
//        return new BoardItem(title, content, null);
//    }

}
