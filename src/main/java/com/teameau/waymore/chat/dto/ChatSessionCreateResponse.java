package com.teameau.waymore.chat.dto;

import com.teameau.waymore.chat.domain.ChatSession;

import java.time.LocalDateTime;

public record ChatSessionCreateResponse(
        Long sessionId,
        String title,
        LocalDateTime createdAt
) {
    public static ChatSessionCreateResponse from(ChatSession chatSession) {
        return new ChatSessionCreateResponse(
                chatSession.getSessionId(),
                chatSession.getTitle(),
                chatSession.getCreatedAt()
        );
    }
}
