package com.drag_race_backend.drag_race.DTOs;

public class RaceResponse {
    private String type;
    private long startTime;

    public RaceResponse(String type, long startTime) {
        this.type = type;
        this.startTime = startTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}