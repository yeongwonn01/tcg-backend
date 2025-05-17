package com._studio.tcg_backend.service;

import com._studio.tcg_backend.repository.PlayerRepository;
import com._studio.tcg_backend.domain.Player;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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

}
