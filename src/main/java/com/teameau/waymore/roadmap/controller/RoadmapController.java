package com.teameau.waymore.roadmap.controller;


import com.teameau.waymore.common.CommonResponseDto;
import com.teameau.waymore.roadmap.dto.RoadmapItemRequest;
import com.teameau.waymore.roadmap.dto.RoadmapItemResponse;
import com.teameau.waymore.roadmap.dto.RoadmapRecommendationResponse;
import com.teameau.waymore.roadmap.dto.RoadmapResponse;
import com.teameau.waymore.roadmap.service.RoadmapService;
import com.teameau.waymore.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "로드맵", description = "로드맵 관련 컨트롤러")
@RestController
@RequestMapping("/api/roadmap")
@RequiredArgsConstructor
public class RoadmapController {
    private final RoadmapService roadmapService;

    @GetMapping("/")
    @Operation(summary = "로드맵 조회", security = { @SecurityRequirement(name = "bearerAuth")})
    public CommonResponseDto<RoadmapResponse> getRoadmap(@AuthenticationPrincipal Long userId) {
        return CommonResponseDto.success(roadmapService.getRoadmap(userId));
    }

    @GetMapping("/{itemId}")
    @Operation(summary = "특정 로드맵 아이템 조회", security = { @SecurityRequirement(name = "bearerAuth")})
    public CommonResponseDto<RoadmapItemResponse> getRoadmapItem(@PathVariable Long itemId) {
        return CommonResponseDto.success(roadmapService.getRoadmapItem(itemId));
    }

    // 로드맵 아이템 추기
    @PostMapping("/item")
    @Operation(summary = "로드맵 아이템 추가", security = { @SecurityRequirement(name = "bearerAuth")})
    public CommonResponseDto<Void> createRoadmapItem(@AuthenticationPrincipal Long userId, @RequestBody RoadmapItemRequest request) {
        roadmapService.createRoadmapItem(userId, request);
        return CommonResponseDto.success();

    }

    // 로드맵 아이템 수정
    @PatchMapping("/item/{roadmapItemId}")
    @Operation(summary = "로드맵 아이템 수정", security = { @SecurityRequirement(name = "bearerAuth")})
    public CommonResponseDto<RoadmapItemResponse> updateRoadmapItem(@AuthenticationPrincipal Long userId, @PathVariable Long roadmapItemId, @RequestBody RoadmapItemRequest request) {
        RoadmapItemResponse response= roadmapService.updateRoadmapItem(userId, roadmapItemId, request);
        return CommonResponseDto.success(response);
    }

    // 로드맵 아이템 삭제
    @DeleteMapping("item/{roadmapItemId}")
    @Operation(summary = "로드맵 아이템 삭제", security = { @SecurityRequirement(name = "bearerAuth")})
    public CommonResponseDto<Void> deleteRoadmapItem(@PathVariable Long roadmapItemId) {
        roadmapService.deleteRoadmapItem(roadmapItemId);
        return CommonResponseDto.success();
    }

    @DeleteMapping("/{roadmapId}")
    @Operation(summary = "로드맵 삭제", security = { @SecurityRequirement(name = "bearerAuth")})
    public CommonResponseDto<Void> deleteRoadmap(@PathVariable Long roadmapId) {
        roadmapService.deleteRoadmap(roadmapId);
        return CommonResponseDto.success();
    }

    /**
     *
     * @param userId 사용자 식별자
     * @return AI 로드맵 추천 결과
     */
    @PostMapping("/recommend")
    @Operation(summary = "로드맵 아이탬 추천(WAS 미들웨어)", security = { @SecurityRequirement(name = "bearerAuth")})
    public CommonResponseDto<RoadmapRecommendationResponse> recommendRoadmap(@AuthenticationPrincipal Long userId) {
        return CommonResponseDto.success(roadmapService.recommendRoadmap(userId));
    }



}
