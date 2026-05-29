package com.teameau.waymore.careernet.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.teameau.waymore.careernet.exception.CareerNetApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class CareerNetClient {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public CareerNetClient(
            @Value("${CAREERNET_BASE_URL:https://www.career.go.kr}") String baseUrl,
            @Value("${CAREERNET_API_KEY:}") String apiKey
    ) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.objectMapper = JsonMapper.builder().build();
        this.apiKey = apiKey;
    }

    public Map<String, Object> getQuestions(String qno) {
        validateApiKey();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/inspct/openapi/test/questions")
                        .queryParam("apikey", apiKey)
                        .queryParam("q", qno)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .flatMap(body -> Mono.error(new CareerNetApiException("커리어넷 문항 조회 요청 실패: " + response.statusCode() + " " + abbreviate(body)))))
                .bodyToMono(String.class)
                .map(this::parseJson)
                .block();
    }

    public Map<String, Object> createReport(Map<String, Object> payload) {
        validateApiKey();
        return webClient.post()
                .uri("/inspct/openapi/test/report")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(withApiKey(payload))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .flatMap(body -> Mono.error(new CareerNetApiException("커리어넷 결과 요청 실패: " + response.statusCode() + " " + abbreviate(body)))))
                .bodyToMono(String.class)
                .map(this::parseJson)
                .block();
    }

    private Map<String, Object> withApiKey(Map<String, Object> payload) {
        payload.put("apikey", apiKey);
        return payload;
    }

    private void validateApiKey() {
        if (!StringUtils.hasText(apiKey)) {
            throw new CareerNetApiException("CAREERNET_API_KEY가 설정되지 않았습니다.");
        }
    }

    private Map<String, Object> parseJson(String body) {
        try {
            return objectMapper.readValue(body, new TypeReference<>() {});
        } catch (Exception exception) {
            throw new CareerNetApiException("커리어넷 응답을 JSON으로 해석할 수 없습니다: " + abbreviate(body));
        }
    }

    private String abbreviate(String body) {
        if (!StringUtils.hasText(body)) {
            return "";
        }
        String normalized = body.replaceAll("\\s+", " ").trim();
        return normalized.length() > 200 ? normalized.substring(0, 200) + "..." : normalized;
    }
}
