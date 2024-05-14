package com.example.pet;

import android.graphics.Bitmap;

public class TimeLineModel{
    private String hour;
    private Bitmap image;
    private boolean status;

    public TimeLineModel(String hour, Bitmap image) {
        this.hour = hour;
        this.image = image;
        if (image != null){
            this.status = true;
        }else {
            this.status = false;
        }
    }

    public TimeLineModel(String hour){
        this.hour = hour;
        this.image = null;
        this.status = false;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
