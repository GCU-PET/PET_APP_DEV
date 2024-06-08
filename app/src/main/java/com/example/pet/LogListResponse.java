package com.example.pet;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LogListResponse {
    private boolean result;
    @SerializedName("response")
    private List<LogItem> logList;

    public boolean isResult() {
        return result;
    }

    public List<LogItem> getLogList() {
        return logList;
    }

    public class LogItem {
        private int _id;
        private String writer;
        private String status;
        private String date;

        public int get_id() {
            return _id;
        }

        public String getStatus() {
            return status;
        }

        public String getWriter() {
            return writer;
        }

        public String getDate() {
            return date;
        }
    }
}
