package com._studio.tcg_backend.controller;

import com._studio.tcg_backend.dto.GameStartRequest;
import com._studio.tcg_backend.model.GameDeck;
import com._studio.tcg_backend.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.Cookie;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public ResponseEntity<GameDeck> startGame(
            @RequestBody GameStartRequest req,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("playerId") == null) {
            // 쿠키가 없거나, 세션에 playerId가 없으면 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long playerId = (Long) session.getAttribute("playerId");
        GameDeck savedDeck = gameService.startGame(req);
        return ResponseEntity.ok(savedDeck);
    }

    @GetMapping("/deck")
    public ResponseEntity<GameDeck> getUserDeck(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("playerId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long playerId = (Long) session.getAttribute("playerId");
        GameDeck deck = gameService.findDeckByPlayerId(playerId);
        return deck != null
                ? ResponseEntity.ok(deck)
                : ResponseEntity.noContent().build();
    }
}