package com._studio.tcg_backend.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;

@RestController
public class CardFileController {

    @GetMapping("/api/game/cards")
    public ResponseEntity<String> getCardsJson(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("playerId") == null) {
            // 로그인 세션이 없으면 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\":\"Unauthorized\"}");
        }

        try {
            ClassPathResource resource = new ClassPathResource("data/cards.json");
            byte[] bytes = Files.readAllBytes(resource.getFile().toPath());
            String json = new String(bytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(json);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"카드 파일을 읽을 수 없습니다.\"}");
        }
    }

}
