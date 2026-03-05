package com.drag_race_backend.drag_race.DTOs;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RaceMessage {
    private String type;
    private Long startTime;
    private List<String> results;

    // YENİ: Android'den gelecek olan oyuncu bilgileri
    private String playerName;
    private long reactionTime;

    // Koordinatlar
    private double startLat;
    private double startLng;
    private double endLat;
    private double endLng;
    private double distanceMeters;

    // 1. KRİTİK EKSİK: Parametresiz Constructor
    // Spring Boot (Jackson), JSON'ı bu sınıfa çevirirken buna ihtiyaç duyar.
    public RaceMessage() {
    }

    // Mevcut Constructor (Opsiyonel olarak güncelleyebilirsin)
    public RaceMessage(String type, Long startTime, List<String> results) {
        this.type = type;
        this.startTime = startTime;
        this.results = results;
    }

    // 2. KRİTİK EKSİK: Yeni alanlar için Getter ve Setter'lar
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public long getReactionTime() {
        return reactionTime;
    }

    public void setReactionTime(long reactionTime) {
        this.reactionTime = reactionTime;
    }

    // Mevcut Getter/Setter'lar (Doğru görünüyor)
    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getStartLng() {
        return startLng;
    }

    public void setStartLng(double startLng) {
        this.startLng = startLng;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLng() {
        return endLng;
    }

    public void setEndLng(double endLng) {
        this.endLng = endLng;
    }

    public double getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(double distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    } // Setter eklemek iyi olur

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }
}