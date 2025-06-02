package com._studio.tcg_backend.controller;

import com._studio.tcg_backend.dto.DeckSaveRequest;
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
            HttpServletRequest httpReq
    ) {
        HttpSession session = httpReq.getSession(false);
        if (session == null || session.getAttribute("playerId") == null) {
            // 쿠키가 없거나, 세션에 playerId가 없으면 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long playerId = (Long) session.getAttribute("playerId");
        // service 메서드 시그니처에 맞춰 playerId를 함께 넘겨줍니다.
        GameDeck savedDeck = gameService.startGame(req, playerId);
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
    @PostMapping("/deck")
    public ResponseEntity<?> saveUserDeck(
            @RequestBody DeckSaveRequest req,
            HttpServletRequest httpReq
    ) {
        HttpSession session = httpReq.getSession(false);
        if (session == null || session.getAttribute("playerId") == null) {
            // 로그인 세션이 없으면 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long playerId = (Long) session.getAttribute("playerId");

        // 1) 기존에 해당 playerId로 저장된 덱이 있는지 조회
        GameDeck existingDeck = gameService.findDeckByPlayerId(playerId);

        if (existingDeck == null) {
            // 2) 덱이 아직 없었다면, 새로 생성
            GameDeck newDeck = new GameDeck(playerId, req.getCardIDs());
            gameService.saveNewDeck(newDeck);
        } else {
            // 3) 이미 덱이 있다면, 목록(cardIDs)만 교체해서 업데이트
            existingDeck.setCardIDs(req.getCardIDs());
            gameService.updateDeck(existingDeck);
        }

        return ResponseEntity.ok().build();
    }

}