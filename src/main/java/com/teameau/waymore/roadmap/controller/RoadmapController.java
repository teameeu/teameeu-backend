package com.teameau.waymore.roadmap.controller;


import com.teameau.waymore.roadmap.service.RoadmapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "로드맵", description = "로드맵 관련 CRUD")
@RestController
@RequestMapping("/api/roadmap/{userId}")
@RequiredArgsConstructor
public class RoadmapController {
    private final RoadmapService roadmapService;

    // 로드맵 조회
    @GetMapping("/")
    @Operation(summary = "로드맵 조회", security = { @SecurityRequirement(name = "bearerAuth")})
    // TODO

    // 특정 로드맵 아이템 조회
    @GetMapping("/{itemId}")
    @Operation(summary = "특정 로드맵 아이템 조회", security = { @SecurityRequirement(name = "bearerAuth")})
    // TODO

    // 로드맵 아이템 추기
    @PostMapping("/item")
    @Operation(summary = "로드맵 아이템 추가", security = { @SecurityRequirement(name = "bearerAuth")})
    public void createRoadmapItem() {
        // TODO
    }

    // 로드맵 아이템 수정
    @PatchMapping("/item/{roadmapItemId}")
    @Operation(summary = "로드맵 아이템 수정", security = { @SecurityRequirement(name = "bearerAuth")})
    public void updateRoadmapItem() {
        // TODO
    }

    // 로드맵 아이템 삭제
    @DeleteMapping("item/{roadmapItemId}")
    @Operation(summary = "로드맵 아이템 삭제", security = { @SecurityRequirement(name = "bearerAuth")})
    public void deleteRoadmapItem() {
        // TODO
    }

    // 로드맵 아이템 추천

}
