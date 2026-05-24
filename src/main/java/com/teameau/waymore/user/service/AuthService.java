package com.teameau.waymore.user.service;

import com.teameau.waymore.common.exception.BusinessException;
import com.teameau.waymore.common.exception.ErrorCode;
import com.teameau.waymore.roadmap.domain.Roadmap;
import com.teameau.waymore.roadmap.repository.RoadmapRepository;
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
    private final RoadmapRepository roadmapRepository;

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
                .userName(request.userName())
                .password(encodedPassword)
                .birthday(request.birthday())
                .department(request.department())
                .career(request.career())
                .build();

        User savedUser = userRepository.save(user);

        // 로드맵 상성
        Roadmap roadmap = Roadmap.builder()
                .user(savedUser)
                .title(savedUser.getDepartment() + " " + savedUser.getCareer() + " 로드맵")
                .build();

        roadmapRepository.save(roadmap);

        // 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(savedUser);
        String refreshToken = jwtTokenProvider.createRefreshToken(savedUser);

        refreshTokenService.save(savedUser.getUserId(), refreshToken, jwtTokenProvider.getRefreshTokenExpiration());

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
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        refreshTokenService.save(user.getUserId(), refreshToken, jwtTokenProvider.getRefreshTokenExpiration());

        return new LoginResult(
            LoginResponse.of(user, accessToken),
            refreshToken,
            jwtTokenProvider.getRefreshTokenExpiration()
        );
    }

    @Transactional
    public LoginResult refresh(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken) || !jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        Long userId = jwtTokenProvider.getUserId(refreshToken);
        String savedRefreshToken = refreshTokenService.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));

        if (!savedRefreshToken.equals(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtTokenProvider.createAccessToken(user);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user);
        refreshTokenService.save(userId, newRefreshToken, jwtTokenProvider.getRefreshTokenExpiration());

        return new LoginResult(
                LoginResponse.of(user, newAccessToken),
                newRefreshToken,
                jwtTokenProvider.getRefreshTokenExpiration()
        );
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenService.delete(userId);
    }
}
