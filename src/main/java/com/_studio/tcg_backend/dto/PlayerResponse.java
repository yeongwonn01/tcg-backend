package com._studio.tcg_backend.dto;

import com._studio.tcg_backend.domain.Player;

public class PlayerResponse {
    private Long id;
    private String username;
    private String nickname;
    private int level;
    private int gold;
    private int gems;

    public PlayerResponse(Player player) {
        this.id = player.getId();
        this.username = player.getUsername();
        this.nickname = player.getNickname();
        this.level = player.getLevel();
        this.gold = player.getGold();
        this.gems = player.getGems();
    }

    // getter 생략하지 말고 붙이기
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getNickname() { return nickname; }
    public int getLevel() { return level; }
    public int getGold() { return gold; }
    public int getGems() { return gems; }
}
