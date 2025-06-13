package com._studio.tcg_backend.dto;

public class CardUpgradeRequest {
    private int cardId;
    private int upgradeCount; // 업그레이드할 카드 수

    public CardUpgradeRequest() {}

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public int getUpgradeCount() {
        return upgradeCount;
    }

    public void setUpgradeCount(int upgradeCount) {
        this.upgradeCount = upgradeCount;
    }
} 