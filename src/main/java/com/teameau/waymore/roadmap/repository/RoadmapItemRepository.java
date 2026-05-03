package com.teameau.waymore.roadmap.repository;

import com.teameau.waymore.roadmap.domain.RoadmapItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadmapItemRepository extends JpaRepository<RoadmapItem, Long> {
    List<RoadmapItem> findAllByRoadmapRoadmapId(Long roadmapId);
}
