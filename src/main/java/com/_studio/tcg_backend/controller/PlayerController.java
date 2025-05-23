package com._studio.tcg_backend.controller;

import com._studio.tcg_backend.domain.Player;
import com._studio.tcg_backend.service.PlayerService; // ✅ 필수 import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com._studio.tcg_backend.dto.RegisterRequest;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

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

        // Getter 생략 가능 (Lombok 사용 시 @Getter 붙여도 됨)
    }

    @Autowired
    private PlayerService playerService;

    @PostMapping("/register")
    public Player register(@RequestBody RegisterRequest request) {
        Player player = new Player();
        player.setUsername(request.getUsername());
        player.setPassword(request.getPassword());
        player.setNickname(request.getNickname());
        player.setLevel(1);
        player.setGold(500);
        player.setGems(100);

        return playerService.registerPlayer(player);
    }


    @PostMapping("/login")
    public ResponseEntity<PlayerResponse> login(@RequestBody Player loginPlayer) {
        return playerService.login(loginPlayer.getUsername(), loginPlayer.getPassword())
                .map(player -> ResponseEntity.ok(new PlayerResponse(player)))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

}

