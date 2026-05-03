package com.teameau.waymore.roadmap.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RoadmapRecommendationResponse {
    private String recommendedTitle;
    private List<ItemRecommendation> items;

    @Getter
    @NoArgsConstructor
    public static class ItemRecommendation {
        private String title;
        private String description;
    }
}
