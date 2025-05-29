package com._studio.tcg_backend.repository;

import com._studio.tcg_backend.model.GameDeck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameDeckRepository extends JpaRepository<GameDeck, Long> {
}