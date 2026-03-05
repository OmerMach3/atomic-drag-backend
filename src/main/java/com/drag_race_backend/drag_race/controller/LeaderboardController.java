package com.drag_race_backend.drag_race.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drag_race_backend.drag_race.model.RaceResult;
import com.drag_race_backend.drag_race.repository.RaceResultRepository;

@RestController
public class LeaderboardController {
    private final RaceResultRepository repository;

    public LeaderboardController(RaceResultRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/leaderboard")
    public List<RaceResult> getLeaderboard() {
        return repository.findByJumpStartFalseOrderByReactionTimeMsAsc();
    }
}
