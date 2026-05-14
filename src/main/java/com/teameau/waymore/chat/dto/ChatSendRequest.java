package com.teameau.waymore.chat.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * STOMP 메세지 전송 시, 클라이언트 요청 DTO
 * @param sessionId 채팅방 식별자, 없으면 새 채팅방 생성
 * @param content 사용자 입력 메세지
 */
public record ChatSendRequest(
        Long sessionId,
        @NotBlank(message = "메세지 내용은 비어있을 수 없습니다.")
        String content
) {
}
