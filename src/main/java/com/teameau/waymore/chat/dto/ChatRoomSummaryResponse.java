package com.teameau.waymore.chat.dto;

import com.teameau.waymore.chat.domain.ChatSession;

import java.time.LocalDateTime;

public record ChatRoomSummaryResponse(
        Long sessionId,
        String title,
        LocalDateTime lastMessageAt
) {

    public static ChatRoomSummaryResponse from(ChatSession chatSession) {
        return new ChatRoomSummaryResponse(
                chatSession.getSessionId(),
                chatSession.getTitle(),
                chatSession.getLastMessageAt()
        );
    }
}
