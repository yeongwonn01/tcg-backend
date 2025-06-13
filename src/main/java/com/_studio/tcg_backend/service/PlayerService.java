package com._studio.tcg_backend.service;

import com._studio.tcg_backend.repository.PlayerRepository;
import com._studio.tcg_backend.domain.Player;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Random random = new Random();

    // 카드 뽑기 비용 (다이아)
    private static final int DRAW_COST = 100;
    // 카드 업그레이드 비용 (골드)
    private static final int UPGRADE_COST = 1000;
    // 최대 카드 보유 수
    private static final int MAX_CARD_COUNT = 3;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // 회원가입 (비밀번호 해싱 후 저장)
    public Player registerPlayer(Player player) {
        player.setPassword(passwordEncoder.encode(player.getPassword()));
        return playerRepository.save(player);
    }

    // 로그인 (비밀번호 검증)
    public Optional<Player> login(String username, String rawPassword) {
        Optional<Player> playerOptional = playerRepository.findByUsername(username);

        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();

            if (player.getPassword() == null) {
                return Optional.empty(); // 비밀번호가 DB에 null일 경우 방어
            }

            if (passwordEncoder.matches(rawPassword, player.getPassword())) {
                return Optional.of(player);
            }
        }

        return Optional.empty(); // 유저 없음 또는 비밀번호 불일치
    }

    @Transactional
    public List<Integer> drawCards(Long playerId, int drawCount) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        // 다이아 확인
        int totalCost = DRAW_COST * drawCount;
        if (player.getGems() < totalCost) {
            throw new RuntimeException("Not enough gems");
        }

        // 다이아 차감
        player.setGems(player.getGems() - totalCost);

        // 카드 뽑기
        List<Integer> drawnCards = new ArrayList<>();
        for (int i = 0; i < drawCount; i++) {
            // 101부터 305까지의 카드 ID 중에서 랜덤 선택
            int cardId = 101 + random.nextInt(205);
            
            // 현재 보유 수 확인
            int currentCount = player.getCardCount(cardId);
            if (currentCount < MAX_CARD_COUNT) {
                player.addCard(cardId, 1);
                drawnCards.add(cardId);
            }
        }

        playerRepository.save(player);
        return drawnCards;
    }

    @Transactional
    public boolean upgradeCard(Long playerId, int cardId, int upgradeCount) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        // 골드 확인
        int totalCost = UPGRADE_COST * upgradeCount;
        if (player.getGold() < totalCost) {
            throw new RuntimeException("Not enough gold");
        }

        // 카드 보유 수 확인
        int currentCount = player.getCardCount(cardId);
        if (currentCount < upgradeCount) {
            throw new RuntimeException("Not enough cards");
        }

        // 골드 차감
        player.setGold(player.getGold() - totalCost);

        // 카드 제거
        player.removeCard(cardId, upgradeCount);

        // 업그레이드된 카드 추가 (카드 ID + 1000으로 업그레이드된 카드 ID 생성)
        int upgradedCardId = cardId + 1000;
        player.addCard(upgradedCardId, 1);

        playerRepository.save(player);
        return true;
    }

    @Transactional(readOnly = true)
    public Map<Integer, Integer> getPlayerCards(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        return player.getOwnedCards();
    }
}
