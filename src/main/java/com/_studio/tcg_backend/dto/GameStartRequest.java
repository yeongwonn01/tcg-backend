package com._studio.tcg_backend.dto;

import java.util.List;

public class GameStartRequest {
    private List<Integer> cardIDs;

    public List<Integer> getCardIDs() {
        return cardIDs;
    }

    public void setCardIDs(List<Integer> cardIDs) {
        this.cardIDs = cardIDs;
    }
}