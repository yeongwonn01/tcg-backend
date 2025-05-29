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
    public GameDeck startGame(GameStartRequest req) {
        GameDeck deck = new GameDeck(req.getCardIDs());
        return deckRepo.save(deck);
    }
}