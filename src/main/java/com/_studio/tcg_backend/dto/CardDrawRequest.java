package com._studio.tcg_backend.dto;

public class CardDrawRequest {
    private int drawCount; // 한 번에 뽑을 카드 수

    public CardDrawRequest() {}

    public int getDrawCount() {
        return drawCount;
    }

    public void setDrawCount(int drawCount) {
        this.drawCount = drawCount;
    }
} 