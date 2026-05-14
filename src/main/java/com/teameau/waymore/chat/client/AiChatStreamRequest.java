package com.teameau.waymore.chat.client;

import com.teameau.waymore.chat.dto.ChatMessageResponse;

import java.util.List;

public record AiChatStreamRequest(
        Long userId,
        Long sessionId,
        Long userMessageId,
        String content,
        List<ChatMessageResponse> history
) {
}
