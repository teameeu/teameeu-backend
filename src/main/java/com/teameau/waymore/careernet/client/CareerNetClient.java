package com.teameau.waymore.careernet.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class CareerNetClient {
    private final WebClient webClient;
    private final String apiKey;

    public CareerNetClient(
            @Value("${CAREERNET_BASE_URL:https://www.career.go.kr}") String baseUrl,
            @Value("${CAREERNET_API_KEY}") String apiKey
    ) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.apiKey = apiKey;
    }

    public JsonNode getQuestions(String qno) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/inspct/openapi/test/questions")
                        .queryParam("apikey", apiKey)
                        .queryParam("q", qno)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    public JsonNode createReport(Map<String, Object> payload) {
        return webClient.post()
                .uri("/inspct/openapi/test/report")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(withApiKey(payload, "apikey"))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    private Map<String, Object> withApiKey(Map<String, Object> payload, String keyName) {
        payload.put(keyName, apiKey);
        return payload;
    }
}
