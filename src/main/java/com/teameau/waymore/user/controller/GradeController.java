package com.teameau.waymore.user.controller;

import com.teameau.waymore.common.CommonResponseDto;
import com.teameau.waymore.user.dto.GradeRequest;
import com.teameau.waymore.user.dto.GradeResponse;
import com.teameau.waymore.user.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "성적 관련 API", description = "성적 등록/조회/삭제/수정")
@RestController
@RequestMapping("/api/grade")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    /**
     * 성적 등록 컨트롤러
     * @param userId
     * @param request
     * @return
     */
    @PostMapping("/")
    @Operation(summary = "성적 등록", security = { @SecurityRequirement(name = "bearerAuth")})
    public CommonResponseDto<GradeResponse> createGrade(@AuthenticationPrincipal Long userId, @Valid @RequestBody GradeRequest request) {
        GradeResponse response = gradeService.createGrade(userId, request);
        return CommonResponseDto.success(response);
    }


    /**
     * 특정 사용자 전체 성적 조회
     * @param userId
     * @return
     */
    @GetMapping("/")
    @Operation(summary = "특정 사용자 전체 성적 조회", security = { @SecurityRequirement(name = "bearerAuth")})
    public CommonResponseDto<List<GradeResponse>> getGrade(@AuthenticationPrincipal Long userId) {
        List<GradeResponse> response = gradeService.getGrade(userId);
        return CommonResponseDto.success(response);
    }

    /**
     * 특정 성적 삭제
     * @param gradeId
     * @param userId
     * @param request
     * @return
     */
    @PatchMapping("/{gradeId}")
    @Operation(summary = "성적 수정", security = { @SecurityRequirement(name = "bearerAuth")})
    public CommonResponseDto<GradeResponse> updateGrade(@PathVariable Long gradeId, @AuthenticationPrincipal Long userId, @Valid @RequestBody GradeRequest request) {
        GradeResponse response = gradeService.updateGrade(userId, gradeId, request);
        return CommonResponseDto.success(response);

    }

    @DeleteMapping("/{gradeId}")
    @Operation(summary = "성적 삭제", security = { @SecurityRequirement(name = "bearerAuth")})
    public CommonResponseDto<Void> deleteGrade(@AuthenticationPrincipal Long userId, @PathVariable Long gradeId) {
        gradeService.deleteGrade(userId, gradeId);
        return CommonResponseDto.success();
    }

}
