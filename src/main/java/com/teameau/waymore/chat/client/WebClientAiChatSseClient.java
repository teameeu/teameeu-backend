package com.teameau.waymore.chat.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class WebClientAiChatSseClient implements AiChatSseClient{
    private final WebClient webClient;
    private final String serviceToken;

    public WebClientAiChatSseClient(
            @Value("${ai.server.base-url}") String baseUrl,
            @Value("${ai.server.service-token}") String serviceToken
    ) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.serviceToken = serviceToken;
    }

    @Override
    public Flux<AiChatStreamEvent> stream(AiChatStreamRequest request) {
        return webClient.post()
                .uri("/internal/ai/chat/stream")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + serviceToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(new org.springframework.core.ParameterizedTypeReference<ServerSentEvent<String>>() {})
                .map(this::toEvent);

    }


    /**
     * 수신한 SSE 이벤트를 내부 dto 변환
     * @param event 수시한 sse 이벤트
     * @return 내부 스트림 dto
     */
    private AiChatStreamEvent toEvent(ServerSentEvent<String> event) {
        String eventName = event.event() == null ? "chunk" : event.event();
        String data = event.data();

        if ("error".equals(eventName)) {
            return new AiChatStreamEvent(eventName, null, data);
        }

        if ("done".equals(eventName)) {
            return new AiChatStreamEvent(eventName, data, null);
        }

        return new AiChatStreamEvent(eventName, data, null);
    }
}
