package com.drag_race_backend.drag_race.DTOs;

public class ReactionRequest {
    private String playerName;
    private long reactionMs;
    private double distance; // YENİ EKLENEN ALAN

    // 1. Spring Boot'un JSON işlemleri için boş constructor (Best Practice)
    public ReactionRequest() {
    }

    // 2. handleReaction içinde tek satırda obje yaratmak için dolu constructor
    public ReactionRequest(String playerName, long reactionMs, double distance) {
        this.playerName = playerName;
        this.reactionMs = reactionMs;
        this.distance = distance;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public long getReactionMs() {
        return reactionMs;
    }

    public void setReactionMs(long reactionMs) {
        this.reactionMs = reactionMs;
    }

    // YENİ: Distance için Getter ve Setter
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    // YENİ: Android'e gidecek olan String formatı
    @Override
    public String toString() {
        // Çıktı: "Can: 215ms (402.5m)"
        return String.format("%s: %dms (%.1fm)", playerName, reactionMs, distance);
    }
}