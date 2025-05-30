package com._studio.tcg_backend.repository;

import com._studio.tcg_backend.model.GameDeck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameDeckRepository extends JpaRepository<GameDeck, Long> {
    // playerId로 덱 조회 (Optional 로 감싸면 없을 때 처리하기 편합니다)
    Optional<GameDeck> findByPlayerId(Long playerId);
}