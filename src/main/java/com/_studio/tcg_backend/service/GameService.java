package com._studio.tcg_backend.service;
import com._studio.tcg_backend.dto.GameStartRequest;
import com._studio.tcg_backend.model.GameDeck;
import com._studio.tcg_backend.repository.GameDeckRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {
    private final GameDeckRepository deckRepo;

    public GameService(GameDeckRepository deckRepo) {
        this.deckRepo = deckRepo;
    }

    @Transactional
    public GameDeck startGame(GameStartRequest req, Long playerId) {
        // 기존 로직에 playerId 전달
        GameDeck deck = new GameDeck(playerId, req.getCardIDs());
        return deckRepo.save(deck);
    }

    // ← 새로 추가
    @Transactional(readOnly = true)
    public GameDeck findDeckByPlayerId(Long playerId) {
        return deckRepo
                .findByPlayerId(playerId)
                .orElse(null);  // 덱이 없으면 null 반환하거나, 예외 던질 수 있습니다
    }

    @Transactional
    public void saveNewDeck(GameDeck newDeck) {
        deckRepo.save(newDeck);
    }

    @Transactional
    public void updateDeck(GameDeck existingDeck) {
        // JPA 엔티티이므로, setCardIDs 후 save()를 호출하거나,
        // flush() → 트랜잭션 커밋 시 변경사항이 자동 반영됩니다.
        deckRepo.save(existingDeck);
    }
}