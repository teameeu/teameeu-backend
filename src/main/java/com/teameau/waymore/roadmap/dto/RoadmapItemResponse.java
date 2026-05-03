package com.teameau.waymore.roadmap.dto;

import com.teameau.waymore.roadmap.domain.RoadmapItem;
import com.teameau.waymore.roadmap.domain.RoadmapItemStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class RoadmapItemResponse {
    private Long roadmapItemId;
    private String title;
    private String description;
    private LocalDate startedAt;
    private LocalDate endedAt;
    private RoadmapItemStatus status;

    /**
     * 엔티티를 dto로 변환
     * @param item 로드맵 아이템 레코드
     * @return DTO
     */
    public static RoadmapItemResponse from(RoadmapItem item) {
        return RoadmapItemResponse.builder()
                .roadmapItemId(item.getRoadmapItemId()) // FIXME
                .title(item.getTitle())
                .description(item.getDescription())
                .startedAt(item.getStartedAt())
                .endedAt(item.getEndedAt())
                .status(item.getStatus())
                .build();
    }
}
