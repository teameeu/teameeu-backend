package com.teameau.waymore.chat.dto;

import com.teameau.waymore.chat.domain.ChatSession;

import java.util.List;

/**
 * 특정 채팅방 메세지 전체 내역 조회 응답 DTO
 */
public record ChatRoomDetailResponse(
        Long sessionId,
        String title,
        List<ChatMessageResponse> messages
) {

    public static ChatRoomDetailResponse of(ChatSession chatSession, List<ChatMessageResponse> messages) {
        return new ChatRoomDetailResponse(
                chatSession.getSessionId(),
                chatSession.getTitle(),
                messages
        );
    }
}
