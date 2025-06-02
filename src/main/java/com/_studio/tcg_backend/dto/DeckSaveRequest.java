package com._studio.tcg_backend.dto;

import java.util.List;

public class DeckSaveRequest {
    private List<Integer> cardIDs;

    public DeckSaveRequest() {}

    public List<Integer> getCardIDs() {
        return cardIDs;
    }

    public void setCardIDs(List<Integer> cardIDs) {
        this.cardIDs = cardIDs;
    }
}
