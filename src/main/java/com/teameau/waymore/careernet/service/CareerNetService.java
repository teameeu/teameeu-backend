package com.teameau.waymore.careernet.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.teameau.waymore.careernet.client.CareerNetClient;
import com.teameau.waymore.careernet.dto.CareerNetReportRequest;
import com.teameau.waymore.careernet.dto.CareerNetTestResponse;
import com.teameau.waymore.careernet.exception.CareerNetApiException;
import com.teameau.waymore.common.exception.BusinessException;
import com.teameau.waymore.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CareerNetService {
    private final CareerNetClient careerNetClient;

    public List<CareerNetTestResponse> getTests() {
        return CareerNetTestCatalog.getTests();
    }

    public JsonNode getQuestions(String qno) {
        if (!StringUtils.hasText(qno)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        JsonNode response = careerNetClient.getQuestions(qno);
        ensureV1Success(response);
        return response;
    }

    public JsonNode createReport(CareerNetReportRequest request) {
        if (!StringUtils.hasText(request.answers())) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("qestrnSeq", request.qno());
        payload.put("trgetSe", request.trgetSe());
        payload.put("gender", request.gender());
        payload.put("school", request.school());
        payload.put("grade", request.grade());
        payload.put("startDtm", request.startDtm());
        payload.put("answers", request.answers());

        JsonNode response = careerNetClient.createReport(payload);
        ensureV1Success(response);
        return response;
    }

    private void ensureV1Success(JsonNode response) {
        if (response == null || !"Y".equals(response.path("SUCC_YN").asText())) {
            String reason = response == null ? "커리어넷 응답이 비어 있습니다." : response.path("ERROR_REASON").asText("커리어넷 요청이 실패했습니다.");
            throw new CareerNetApiException(reason);
        }
    }
}
