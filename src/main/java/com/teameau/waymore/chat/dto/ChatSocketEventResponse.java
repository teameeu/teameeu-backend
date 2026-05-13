package com.teameau.waymore.chat.dto;

public record ChatSocketEventResponse(
        String type,
        Long sessionId,
        Long userMessageId,
        Long assistantMessageId,
        String content,
        String message
) {
    // TODO: javadoc 작성
    /**
     * 사용자 채팅 메세지 이벤트 생성
     * @param sessionId
     * @param userMessageId
     * @param assistantMessageId
     * @return
     */
    public static ChatSocketEventResponse ack(Long sessionId, Long userMessageId, Long assistantMessageId) {
        return new ChatSocketEventResponse("chat.ack", sessionId, userMessageId, assistantMessageId, null, null);
    }

    /**
     * AI 응답 스트리밍 시작 이벤트 생성
     * @param sessionId
     * @param assistantMessageId
     * @return
     */
    public static ChatSocketEventResponse start(Long sessionId, Long assistantMessageId) {
        return new ChatSocketEventResponse("ai.start", sessionId, null, assistantMessageId, null, null);
    }

    /**
     * AI 응답 청크 이벤트 생성
     * @param sessionId
     * @param assistantMessageId
     * @param content
     * @return
     */
    public static ChatSocketEventResponse chunk(Long sessionId, Long assistantMessageId, String content) {
        return new ChatSocketEventResponse("ai.chunk", sessionId, null, assistantMessageId, content, null);
    }

    /**
     * AI 응답 완료 이벤트 생성
     */
    public static ChatSocketEventResponse done(Long sessionId, Long assistantMessageId, String content) {
        return new ChatSocketEventResponse("ai.done", sessionId, null, assistantMessageId, content, null);
    }


    /**
     * AI 처리 오류 이벤트 생성
     */
    public static ChatSocketEventResponse error(Long sessionId, Long assistantMessageId, String message) {
        return new ChatSocketEventResponse("ai.error", sessionId, null, assistantMessageId, null, message);
    }
}
