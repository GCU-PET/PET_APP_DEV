package com.example.pet;

public class UserInfoResponse {
    private boolean result;
    private ResponseData response;

    public boolean isResult() {
        return result;
    }

    public ResponseData getResponse() {
        return response;
    }

    public static class ResponseData {
        private String _id;
        private String ID;
        private String PW;
        private String userName;
        private String petName;

        public String get_id() {
            return _id;
        }

        public String getID() {
            return ID;
        }

        public String getPW() {
            return PW;
        }

        public String getUserName() {
            return userName;
        }

        public String getPetName() {
            return petName;
        }
    }
}
