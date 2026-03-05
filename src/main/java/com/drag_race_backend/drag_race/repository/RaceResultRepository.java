package com.drag_race_backend.drag_race.repository;

import com.drag_race_backend.drag_race.model.RaceResult;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaceResultRepository extends JpaRepository<RaceResult, Long> {
    // Gerekirse kullanıcı adına göre en iyi dereceleri getiren metotlar eklenebilir
    List<RaceResult> findByJumpStartFalseOrderByReactionTimeMsAsc();
}