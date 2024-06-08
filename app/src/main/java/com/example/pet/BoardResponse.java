package com.example.pet;

import java.util.List;

public class BoardResponse {
    private boolean result;
    private List<BoardItem> response;

    // Getters and setters
    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<BoardItem> getResponse() {
        return response;
    }

    public void setResponse(List<BoardItem> response) {
        this.response = response;
    }
}
