package com.teameau.waymore.chat.client;

public record AiChatStreamEvent(
        String event,
        String content,
        String errorMessage
) {

    public boolean isChunk() {
        return "chunk".equals(event);
    }

    public boolean isDone() {
        return "done".equals(event);
    }

    public boolean isError() {
        return "error".equals(event);
    }

}
