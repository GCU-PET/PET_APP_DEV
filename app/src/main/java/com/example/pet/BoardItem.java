package com.example.pet;


public class BoardItem {
    private int _id;
    private String title;
    private String content;
    private String date;
    private String writer;
    private String image;

//    (String title, String date, String userID, String content, Uri imageUri)

    public BoardItem(int _id, String title, String content, String date, String userID, String image) {
        this._id = _id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.writer = userID;
        this.image = image;
    }

    // 이미지가 없을 때
    public BoardItem(String title, String date, String userID) {
        this.title = title;
        this.date = date;
        this.writer = userID;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWriter() { return writer; }
    public void setUserID(String userID) { this.writer = userID; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }


//    public static BoardItem createWithoutImage(String title, String content) {
//        return new BoardItem(title, content, null);
//    }

}
