package com.drag_race_backend.drag_race.DTOs;

public class RaceStatus {
    private long startTime;

    public RaceStatus(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}