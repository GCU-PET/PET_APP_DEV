package com.example.pet;

import android.net.Uri;


public class BoardItem {
    private String title;
    private String content;
    private String date;
    private String userID;
    private String imageUrl;

//    (String title, String date, String userID, String content, Uri imageUri)

    public BoardItem(String title,String content, String date, String userID, String imageUrl) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.userID = userID;
        this.imageUrl = imageUrl;
    }

    // 이미지가 없을 때
    public BoardItem(String title, String date, String userID) {
        this.title = title;
        this.date = date;
        this.userID = userID;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }


//    public static BoardItem createWithoutImage(String title, String content) {
//        return new BoardItem(title, content, null);
//    }

}
