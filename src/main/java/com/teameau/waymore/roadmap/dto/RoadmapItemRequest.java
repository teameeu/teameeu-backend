package com.teameau.waymore.roadmap.dto;

import com.teameau.waymore.roadmap.domain.RoadmapItemStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RoadmapItemRequest {
    private String title;
    private String description;
    private LocalDate startedAt;
    private LocalDate endedAt;

    @Schema(description = "로드맵 아이템 상테", allowableValues = {"TODO", "IN_PROGRESS", "DONE"})
    private RoadmapItemStatus status;

}
