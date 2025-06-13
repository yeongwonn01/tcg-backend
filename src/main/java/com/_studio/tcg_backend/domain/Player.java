package com._studio.tcg_backend.domain;

import jakarta.persistence.*;
import java.util.Map;
import java.util.HashMap;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    private int level;
    private int gold;
    private int gems;

    @ElementCollection
    @CollectionTable(name = "player_cards", joinColumns = @JoinColumn(name = "player_id"))
    @MapKeyColumn(name = "card_id")
    @Column(name = "count")
    private Map<Integer, Integer> ownedCards = new HashMap<>(); // cardId -> count

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    // Getter & Setter (Lombok 없이 직접 생성)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getGems() {
        return gems;
    }

    public void setGems(int gems) {
        this.gems = gems;
    }

    public Map<Integer, Integer> getOwnedCards() {
        return ownedCards;
    }

    public void setOwnedCards(Map<Integer, Integer> ownedCards) {
        this.ownedCards = ownedCards;
    }

    public void addCard(int cardId, int count) {
        ownedCards.merge(cardId, count, Integer::sum);
    }

    public boolean removeCard(int cardId, int count) {
        Integer currentCount = ownedCards.get(cardId);
        if (currentCount == null || currentCount < count) {
            return false;
        }
        if (currentCount == count) {
            ownedCards.remove(cardId);
        } else {
            ownedCards.put(cardId, currentCount - count);
        }
        return true;
    }

    public int getCardCount(int cardId) {
        return ownedCards.getOrDefault(cardId, 0);
    }
}
