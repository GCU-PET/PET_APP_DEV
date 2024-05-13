package com.example.pet;

import android.graphics.Bitmap;

public class TimeLineModel{
    private String date;
    private Bitmap image;
    private boolean status;

    public TimeLineModel(String date, Bitmap image) {
        this.date = date;
        this.image = image;
        if (image != null){
            this.status = true;
        }else {
            this.status = false;
        }
    }

    public TimeLineModel(String date){
        this.date = date;
        this.image = null;
        this.status = false;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
