package com.teameau.waymore.user.service;


import com.teameau.waymore.common.exception.BusinessException;
import com.teameau.waymore.common.exception.ErrorCode;
import com.teameau.waymore.user.domain.Grade;
import com.teameau.waymore.user.domain.User;
import com.teameau.waymore.user.dto.GradeRequest;
import com.teameau.waymore.user.dto.GradeResponse;
import com.teameau.waymore.user.repository.GradeRepository;
import com.teameau.waymore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 이건 뭘까?
public class GradeService {
    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;

    /**
     * 성적 등록
     * @param userId 사용자 식별자
     * @param request 성적 입력 요청 dto
     * @return 성적 응답 객체
     */
    @Transactional
    public GradeResponse createGrade(Long userId, GradeRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Grade grade = Grade.builder()
                .user(user)
                .subject(request.subject())
                .score(request.score())
                .grade(request.grade())
                .build();

        Grade savedGrade = gradeRepository.save(grade);

        return GradeResponse.from(savedGrade);
    }

    /**
     * 특정 사용자의 전체 성적 조회
     * @param userId
     * @return
     */
    public List<GradeResponse> getGrade(Long userId) {
        return gradeRepository.findByUser_UserId(userId).stream()
                .map(GradeResponse::from)
                .toList();
    }


    /**
     *
     * @param userId 사용자 식별자
     * @param gradeId 성적 식별자
     * @param request 요청 dto
     * @return 성적 객체 dto
     */
    @Transactional
    public GradeResponse updateGrade(Long userId, Long gradeId, GradeRequest request) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST)); // TODO: 에러코드 만들어주기

        // 소유권 검증
        if (!grade.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST); // TODO: 에러코드 만들까 말까
        }

        grade.update(
                request.subject(),
                request.score(),
                request.grade()
        );

        return GradeResponse.from(grade);
    }

    /**
     * 특정 성적 삭제
     * @param userId 사용자 식별자
     * @param gradeId 성적 식별자
     */
    @Transactional
    public void deleteGrade(Long userId, Long gradeId) {
        Grade grade = gradeRepository.findById(gradeId).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST)); // TODO: 응답 에러 코드

        // 소유권 검증
        if (!grade.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST); // TODO: 에러코드 만들까 말까
        }

        gradeRepository.delete(grade);

    }
}
