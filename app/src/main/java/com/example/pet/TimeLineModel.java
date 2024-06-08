package com.example.pet;

import android.graphics.Bitmap;
import android.net.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeLineModel{
    private String hour;
    //private Bitmap image;
    private String status;

    public TimeLineModel(String hour, String status){
        this.hour = formatDate(hour);
        this.status = status;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String formatDate(String dateStr) {
        // 입력된 형식: 20240605 17:57:20:877
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS");
        // 원하는 출력 형식: 2024.6.5 17:57
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy.M.d HH:mm");

        try {
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr; // 파싱 실패 시 원래 문자열 반환
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
