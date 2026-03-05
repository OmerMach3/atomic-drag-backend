package com.drag_race_backend.drag_race.controller;

import com.drag_race_backend.drag_race.DTOs.PlayerMessage;
import com.drag_race_backend.drag_race.DTOs.RaceMessage;
import com.drag_race_backend.drag_race.DTOs.ReactionRequest;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class RaceController {
    private final Map<String, Boolean> roomFinishRequested = new ConcurrentHashMap<>();
    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String, Set<String>> roomPlayers = new ConcurrentHashMap<>();
    private final Map<String, List<ReactionRequest>> roomResults = new ConcurrentHashMap<>();

    public RaceController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/join/{roomCode}")
    public void joinRoom(@DestinationVariable String roomCode, PlayerMessage playerMsg) {
        roomPlayers.putIfAbsent(roomCode, ConcurrentHashMap.newKeySet());
        roomPlayers.get(roomCode).add(playerMsg.getPlayerName());

        List<String> currentPlayers = new ArrayList<>(roomPlayers.get(roomCode));
        RaceMessage updateMsg = new RaceMessage("LOBBY_UPDATE", 0L, currentPlayers); // 0 -> 0L
        messagingTemplate.convertAndSend("/topic/race/" + roomCode, updateMsg);

        System.out.println(playerMsg.getPlayerName() + " odaya katıldı: " + roomCode);
    }

    @MessageMapping("/start-race/{roomCode}")
    public void startRace(@DestinationVariable String roomCode) {
        roomFinishRequested.remove(roomCode);
        roomResults.put(roomCode, new CopyOnWriteArrayList<>());

        long durationMs = 5000; // sadece süre gönder
        RaceMessage startMsg = new RaceMessage("START", durationMs, null);
        messagingTemplate.convertAndSend("/topic/race/" + roomCode, startMsg);

        System.out.println("Yarış başlatıldı: " + roomCode);
    }

    @MessageMapping("/reaction/{roomCode}")
    public void handleReaction(@DestinationVariable String roomCode, RaceMessage message) {
        System.out.println("handleReaction çağrıldı: roomCode=" + roomCode
                + ", player=" + message.getPlayerName()
                + ", time=" + message.getReactionTime()
                + ", distance=" + message.getDistanceMeters()); // ← log it

        // Use accelerometer distance directly — GPS is unreliable for short races
        double distance = message.getDistanceMeters();

        // Fallback to GPS only if accelerometer distance is 0 (e.g. old client)
        if (distance == 0.0) {
            /*
             * distance = calculateDistance(
             * message.getStartLat(), message.getStartLng(),
             * message.getEndLat(), message.getEndLng());
             */
        }

        ReactionRequest request = new ReactionRequest(
                message.getPlayerName(),
                message.getReactionTime(),
                distance);

        roomResults.computeIfAbsent(roomCode, k -> new CopyOnWriteArrayList<>()).add(request);
        trySendFinish(roomCode);
        System.out.println("Liste güncellendi. Toplam: " + roomResults.get(roomCode).size());
    }

    @MessageMapping("/brake/{roomCode}")
    public void handleBrake(@DestinationVariable String roomCode) {
        System.out.println("handleBrake çağrıldı: roomCode=" + roomCode);
        roomFinishRequested.put(roomCode, true);
        trySendFinish(roomCode);
    }

    /*
     * private double calculateDistance(double lat1, double lon1, double lat2,
     * double lon2) {
     * final int R = 6371;
     * double latDistance = Math.toRadians(lat2 - lat1);
     * double lonDistance = Math.toRadians(lon2 - lon1);
     * 
     * double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
     * + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
     * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
     * 
     * double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
     * return R * c * 1000;
     * }
     */

    private void trySendFinish(String roomCode) {
        List<ReactionRequest> list = roomResults.get(roomCode);
        if (list == null || list.isEmpty())
            return;
        if (!roomFinishRequested.getOrDefault(roomCode, false))
            return;

        System.out.println("FINISH mesajı gönderiliyor. Sonuç sayısı: " + list.size());
        sendFinishMessage(roomCode, formatResults(list));

        // temizle
        roomResults.remove(roomCode);
        roomFinishRequested.remove(roomCode);

        System.out.println("FINISH mesajı gönderildi!");
    }

    private List<String> formatResults(List<ReactionRequest> reactions) {
        List<ReactionRequest> sortableList = new ArrayList<>(reactions);
        sortableList.sort(Comparator.comparingLong(r -> Math.abs(r.getReactionMs())));

        List<String> formattedResults = new ArrayList<>();
        for (int i = 0; i < sortableList.size(); i++) {
            ReactionRequest r = sortableList.get(i);
            String status = r.getReactionMs() < 0 ? "Erken" : "Geç";
            long absDiff = Math.abs(r.getReactionMs());

            String row = String.format("%d. %s: %dms (%s) - %.1fm",
                    (i + 1), r.getPlayerName(), absDiff, status, r.getDistance());
            formattedResults.add(row);
        }

        return formattedResults;
    }

    private void sendFinishMessage(String roomCode, List<String> formattedResults) {
        RaceMessage message = new RaceMessage("FINISH", 0L, formattedResults);
        messagingTemplate.convertAndSend("/topic/race/" + roomCode, message);
    }
}