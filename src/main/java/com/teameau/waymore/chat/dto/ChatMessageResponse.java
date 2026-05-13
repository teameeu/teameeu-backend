package com.teameau.waymore.chat.dto;

import com.teameau.waymore.chat.domain.ChatMessage;
import com.teameau.waymore.chat.domain.ChatMessageRole;

import java.time.LocalDateTime;

/**
 * 채팅 메세지 조회 DTO
 * @param messageId 메세지 식별자
 * @param role 발신자 역할(USER, AI)
 * @param content 메세지 본문
 * @param createdAt 메세지 발송 시간
 */
public record ChatMessageResponse(
        Long messageId,
        ChatMessageRole role,
        String content,
        LocalDateTime createdAt
) {
    public static ChatMessageResponse from(ChatMessage chatMessage) {
        return new ChatMessageResponse(
                chatMessage.getMessageId(),
                chatMessage.getRole(),
                chatMessage.getContent(),
                chatMessage.getCreatedAt()
        );
    }


}
