package com.teameau.waymore.careernet.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.teameau.waymore.careernet.dto.CareerNetReportRequest;
import com.teameau.waymore.careernet.dto.CareerNetTestResponse;
import com.teameau.waymore.careernet.service.CareerNetService;
import com.teameau.waymore.common.CommonResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "커리어넷 진로검사", description = "커리어넷 진로심리검사 연동 API")
@RestController
@RequestMapping("/api/careernet")
@RequiredArgsConstructor
public class CareerNetController {
    private final CareerNetService careerNetService;

    @GetMapping("/tests")
    @Operation(summary = "검사목록조회", security = { @SecurityRequirement(name = "bearerAuth") })
    public CommonResponseDto<List<CareerNetTestResponse>> getTests() {
        return CommonResponseDto.success(careerNetService.getTests());
    }

    @GetMapping("/tests/{qno}/questions")
    @Operation(summary = "진로검사 문항 조회", security = { @SecurityRequirement(name = "bearerAuth") })
    public CommonResponseDto<JsonNode> getQuestionsByPath(@PathVariable String qno) {
        return CommonResponseDto.success(careerNetService.getQuestions(qno));
    }

    @PostMapping("/reports")
    @Operation(summary = "검사결과조회", security = { @SecurityRequirement(name = "bearerAuth") })
    public CommonResponseDto<JsonNode> createReport(@Valid @RequestBody CareerNetReportRequest request) {
        return CommonResponseDto.success(careerNetService.createReport(request));
    }
}
