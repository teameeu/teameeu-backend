package com.teameau.waymore.chat.controller;

import com.teameau.waymore.chat.dto.ChatRoomDetailResponse;
import com.teameau.waymore.chat.dto.ChatRoomListResponse;
import com.teameau.waymore.chat.service.ChatQueryService;
import com.teameau.waymore.common.CommonResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "채팅", description = "AI 채팅 조회 API")
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatQueryService chatQueryService;

    /**
     * 채팅방 목록 조회
     */
    @GetMapping
    @Operation(summary = "채팅방 목록 조회", security = { @SecurityRequirement(name = "bearerAuth")})
    public CommonResponseDto<ChatRoomListResponse> getChatRooms(@AuthenticationPrincipal Long userid) {
        return CommonResponseDto.success(chatQueryService.getChatRoom(userid));
    }


    /**
     * 특정 채팅방 상세 조회
     */

    @GetMapping("/{sessionId}")
    @Operation(summary = "특정 채팅방 상세 조회", security = { @SecurityRequirement(name = "bearerAuth")})
    public CommonResponseDto<ChatRoomDetailResponse> getChatRoom(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long sessionId
    ) {
        return CommonResponseDto.success(chatQueryService.getChatRoom(userId, sessionId));
    }
}
