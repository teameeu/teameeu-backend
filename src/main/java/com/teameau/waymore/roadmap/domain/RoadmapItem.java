package com.teameau.waymore.roadmap.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "\"ROADMAP_ITEM\"")
public class RoadmapItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roadmap_item_id")
    private Long roadmapItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id", nullable = false)
    private Roadmap roadmap;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "started_at", nullable = true)
    private LocalDate startedAt;

    @Column(name = "ended_at", nullable = true)
    private LocalDate endedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RoadmapItemStatus status;

    @Builder
    private RoadmapItem(
            Roadmap roadmap,
            String title,
            String description,
            LocalDate startedAt,
            LocalDate endedAt,
            RoadmapItemStatus status
    ) {
        this.roadmap = roadmap;
        this.title = title;
        this.description = description;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.status = status;
    }
}
