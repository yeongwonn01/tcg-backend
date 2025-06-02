package com._studio.tcg_backend.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;           // 추가
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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
            // 1) 클래스패스 내 리소스 경로. 'data/cards.json'은 src/main/resources/data/cards.json 에 대응
            ClassPathResource resource = new ClassPathResource("data/cards.json");

            // 2) getFile() 대신 getInputStream() 사용 → JAR 환경에서도 동작
            InputStream is = resource.getInputStream();
            byte[] bytes = is.readAllBytes();
            String json = new String(bytes, StandardCharsets.UTF_8);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(json);

        } catch (IOException e) {
            // 읽기 실패 시, 로그를 꼭 남겨서 원인 파악에 도움을 받을 수 있도록 합니다.
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"카드 파일을 읽을 수 없습니다.\"}");
        }
    }
}
