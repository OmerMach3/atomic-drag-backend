package com.drag_race_backend.drag_race.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "race_results")
@Data // Getter, Setter ve ToString'i otomatik oluşturur
public class RaceResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerName; // Xiaomi 15 kullanıcısı veya arkadaşın Aras

    private long reactionTimeMs; // Reaksiyon süresi (milisaniye)

    private boolean jumpStart; // Hatalı kalkış mı?

    private LocalDateTime raceDate; // Yarışın yapıldığı zaman

    public RaceResult() {
        this.raceDate = LocalDateTime.now();
    }
}
