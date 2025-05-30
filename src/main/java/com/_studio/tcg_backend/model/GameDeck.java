package com._studio.tcg_backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class GameDeck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long playerId;
    @ElementCollection
    @CollectionTable(name = "game_deck_cards", joinColumns = @JoinColumn(name = "game_deck_id"))
    @Column(name = "card_id")
    @OrderColumn(name = "card_order")  // 덱 내 카드 순서 보존
    private List<Integer> cardIDs;

    // optional: link to player, timestamp, etc.

    public GameDeck() {}

    public GameDeck(Long playerId, List<Integer> cardIDs) {
        this.playerId = playerId;
        this.cardIDs  = cardIDs;
    }

    public Long getId() {
        return id;
    }

    public List<Integer> getCardIDs() {
        return cardIDs;
    }

    public void setCardIDs(List<Integer> cardIDs) {
        this.cardIDs = cardIDs;
    }
    public Long getPlayerId() {
        return playerId;
    }
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
}