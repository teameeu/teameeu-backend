package com.teameau.waymore.careernet.dto;

import java.util.List;

public record CareerNetTestResponse(
        String qno,
        String name,
        List<String> targetCodes,
        String answerExample,
        String note
) {
}
