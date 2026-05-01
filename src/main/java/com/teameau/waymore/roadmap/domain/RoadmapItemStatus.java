package com.teameau.waymore.roadmap.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoadmapItemStatus {
    TODO("진행 전"),
    IN_PROGRESS("진행 중"),
    DONE("완료");

    private final String description;

}
