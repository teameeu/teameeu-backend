package com.teameau.waymore.user.service;

import com.teameau.waymore.common.exception.BusinessException;
import com.teameau.waymore.common.exception.ErrorCode;
import com.teameau.waymore.user.domain.User;
import com.teameau.waymore.user.dto.*;
import com.teameau.waymore.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    // 회원가입
    @Transactional
    public SignupResult signup(SignupRequest request) {
        // 이메일 중복 검사
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 비밀번호 확인
        if (!request.password().equals(request.passwordCheck())) {
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.builder()
                .email(request.email())
                .password(encodedPassword)
                .birthday(request.birthday())
                .department(request.department())
                .career(request.career())
                .build();

        User savedUser = userRepository.save(user);

        // 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(savedUser);
        String refreshToken = jwtTokenProvider.createRefreshToken(savedUser);

        refreshTokenService.save(savedUser.getId(), refreshToken, jwtTokenProvider.getRefreshTokenExpiration());

        return new SignupResult(
                SignupResponse.of(savedUser, accessToken),
                refreshToken,
                jwtTokenProvider.getRefreshTokenExpiration()
        );


    }


    @Transactional
    public LoginResult login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        // 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createAccessToken(user);

        refreshTokenService.save(user.getId(), refreshToken, jwtTokenProvider.getRefreshTokenExpiration());

        return new LoginResult(
            LoginResponse.of(user, accessToken),
            refreshToken,
            jwtTokenProvider.getRefreshTokenExpiration()
        );
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenService.delete(userId);
    }
}
