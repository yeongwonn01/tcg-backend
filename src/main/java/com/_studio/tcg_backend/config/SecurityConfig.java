package com._studio.tcg_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // API 호출이라 CSRF 토큰 체크는 생략
                .csrf(AbstractHttpConfigurer::disable)

                // CORS 필요하다면 여기에 설정 추가
                // .cors(cors -> cors.configurationSource(corsConfigSource()))

                .authorizeHttpRequests(authz -> authz
                        // 로그인·회원가입만 모두 허용
                        .requestMatchers(HttpMethod.POST, "/api/player/login", "/api/player/register")
                        .permitAll()
                        // 게임 관련 API는 인증된 사용자만
                        .requestMatchers("/api/game/**")
                        .authenticated()
                        // 그 외는 모두 허용
                        .anyRequest()
                        .permitAll()
                )

                // 세션 기반 인증. 필요 시에만 세션 생성
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                // 스프링 기본 로그인 폼 끄기 (우리가 직접 만든 /api/player/login 사용)
                .formLogin(AbstractHttpConfigurer::disable)
                // HTTP Basic 끄기 (사용하지 않으면)
                .httpBasic(AbstractHttpConfigurer::disable)

        ;

        return http.build();
    }
}