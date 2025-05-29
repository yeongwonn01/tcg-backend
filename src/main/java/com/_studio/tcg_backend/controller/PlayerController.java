package com._studio.tcg_backend.controller;

import com._studio.tcg_backend.domain.Player;
import com._studio.tcg_backend.service.PlayerService; // ✅ 필수 import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com._studio.tcg_backend.dto.RegisterRequest;
import com._studio.tcg_backend.dto.PlayerResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;

import jakarta.servlet.http.HttpSession;
@RestController
@RequestMapping("/api/player")
public class PlayerController {

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
    public ResponseEntity<PlayerResponse> login(
            @RequestBody Player loginPlayer,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return playerService.login(loginPlayer.getUsername(), loginPlayer.getPassword())
                .map(player -> {
                    // 1) 세션 생성
                    HttpSession session = request.getSession(true);
                    session.setAttribute("playerId", player.getId());
                    // 2) JSESSIONID 쿠키 직접 설정 (Spring Boot 기본 JSESSIONID 사용)
                    Cookie cookie = new Cookie("JSESSIONID", session.getId());
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    cookie.setSecure(true); // HTTPS 환경에서만 전송
                    cookie.setMaxAge(30 * 60); // 30분
                    response.addCookie(cookie);

                    return ResponseEntity.ok(new PlayerResponse(player));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }


}

