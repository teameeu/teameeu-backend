package com.teameau.waymore.roadmap.dto;

import com.teameau.waymore.roadmap.domain.Roadmap;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class RoadmapResponse {
    private Long roadmapId;
    private String title;
    private LocalDateTime createdAt;
    private List<RoadmapItemResponse> items;

    public static RoadmapResponse from(Roadmap roadmap, List<RoadmapItemResponse> items) {
        return RoadmapResponse.builder()
                .roadmapId(roadmap.getRoadmapId())
                .title(roadmap.getTitle())
                .createdAt(roadmap.getCreatedAt())
                .items(items)
                .build();
    }

}
