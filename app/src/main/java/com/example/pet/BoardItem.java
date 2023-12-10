package com.example.pet;

import android.net.Uri;


public class BoardItem {

    private String title;
    private String content;
    private Uri imageUri;
    private String userID;
    private String date;

    public BoardItem(String title,String userID, String date, String content, Uri imageUri) {
        this.title = title;
        this.content = content;
        this.imageUri = imageUri;
        this.userID = userID;
        this.date = date;

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

    public void setContent(String content) {
        this.content = content;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public static BoardItem createWithoutImage(String title, String content) {
        return new BoardItem(title, content, null);
    }

}
