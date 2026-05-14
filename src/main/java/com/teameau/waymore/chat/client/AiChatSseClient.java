package com.teameau.waymore.chat.client;


import reactor.core.publisher.Flux;

public interface AiChatSseClient {
    Flux<AiChatStreamEvent> stream(AiChatStreamRequest request);
}
