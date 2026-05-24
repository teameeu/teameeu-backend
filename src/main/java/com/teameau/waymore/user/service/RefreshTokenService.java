package com.teameau.waymore.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final StringRedisTemplate redisTemplate;


    // Redis 안에서 refresh token key를 구분하기 위한 prefix
    private static final String REFRESH_TOKEN_KEY_PREFIX = "auth:refresh:";

    // redis에 토큰 저장
    public void save(Long userId, String refreshToken, Duration exp) {
        String key = REFRESH_TOKEN_KEY_PREFIX + userId;

        redisTemplate.opsForValue().set(key, refreshToken, exp);
    }

    // 로그아웃 시, refresh 토큰 삭제
    public void delete(Long userId) {
        String key = REFRESH_TOKEN_KEY_PREFIX + userId;
        redisTemplate.delete(key);
    }

    public Optional<String> findByUserId(Long userId) {
        String key = REFRESH_TOKEN_KEY_PREFIX + userId;
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }
}
