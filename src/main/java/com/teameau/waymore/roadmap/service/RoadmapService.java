package com.teameau.waymore.roadmap.service;

import com.teameau.waymore.common.exception.BusinessException;
import com.teameau.waymore.common.exception.ErrorCode;
import com.teameau.waymore.roadmap.domain.Roadmap;
import com.teameau.waymore.roadmap.domain.RoadmapItem;
import com.teameau.waymore.roadmap.domain.RoadmapItemStatus;
import com.teameau.waymore.roadmap.dto.RoadmapItemRequest;
import com.teameau.waymore.roadmap.dto.RoadmapItemResponse;
import com.teameau.waymore.roadmap.dto.RoadmapRecommendationResponse;
import com.teameau.waymore.roadmap.dto.RoadmapResponse;
import com.teameau.waymore.roadmap.repository.RoadmapItemRepository;
import com.teameau.waymore.roadmap.repository.RoadmapRepository;
import com.teameau.waymore.user.domain.User;
import com.teameau.waymore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoadmapService {
    private final RoadmapRepository roadmapRepository;
    private final RoadmapItemRepository roadmapItemRepository;
    private  final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 로드맵 조회하기
     * @param userId : 사용자명
     * @return
     */
    public RoadmapResponse getRoadmap(Long userId) {
        Roadmap roadmap = roadmapRepository.findByUser_UserId(userId).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));

        List<RoadmapItemResponse> items = roadmapItemRepository.findAllByRoadmapRoadmapId(roadmap.getRoadmapId())
                .stream().map(RoadmapItemResponse::from).collect(Collectors.toList());

        return RoadmapResponse.from(roadmap, items);
    }

    /**
     * 로드맵 아이템 조회
     * @param itemId 로드맵 아이템 식별자
     * @return
     */
    public RoadmapItemResponse getRoadmapItem(Long itemId) {
        RoadmapItem item = roadmapItemRepository.findById(itemId).orElseThrow(() -> new BusinessException(ErrorCode.ROADMAP_NOT_FOUND));

        return RoadmapItemResponse.from(item);
    }

    /**
     * 로드맵 아이템 추가
     * @param userId 사용자 식별자
     * @param request 사용자 입력값 dto
     */
    @Transactional
    public void createRoadmapItem(Long userId, RoadmapItemRequest request) {
        Roadmap roadmap = roadmapRepository.findByUser_UserId(userId).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));

        RoadmapItem item = RoadmapItem.builder()
                .roadmap(roadmap)
                .title(request.getTitle())
                .description(request.getDescription())
                .startedAt(request.getStartedAt())
                .endedAt(request.getEndedAt())
                .status(request.getStatus() != null ? request.getStatus() : RoadmapItemStatus.TODO)
                .build();
        roadmapItemRepository.save(item);
    }

    /**
     * 로드맵 아이템 수정
     * @param roadmapItemId 로드맵 아이템 식별자
     * @param request 수정 내용 body
     */
    @Transactional
    public RoadmapItemResponse updateRoadmapItem(Long userId, Long roadmapItemId, RoadmapItemRequest request) {
        RoadmapItem item = roadmapItemRepository.findById(roadmapItemId).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));

        // 소유권 검증
        if (!item.getRoadmap().getUser().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }
        item.update(
                request.getTitle(),
                request.getDescription(),
                request.getStartedAt(),
                request.getEndedAt(),
                request.getStatus()
        );

        return RoadmapItemResponse.from(item);
    }

    /**
     * 로드맵 아이템 삭제
     * @param roadmapItemId 로드맵 아이템 식별자
     */
    @Transactional
    public void deleteRoadmapItem(Long roadmapItemId) {
        if (!roadmapItemRepository.existsById(roadmapItemId)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }
        roadmapItemRepository.deleteById(roadmapItemId);
    }

    /**
     * 로드맵 삭제
     * @param roadmapId 로드맵 식별자
     */
    @Transactional
    public void deleteRoadmap(Long roadmapId) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));

        List<RoadmapItem> items = roadmapItemRepository.findAllByRoadmapRoadmapId(roadmapId);
        roadmapItemRepository.deleteAll(items);
        roadmapRepository.delete(roadmap);
    }

    @Transactional
    // TODO: AI 서버 연동 로직 구현
    public RoadmapRecommendationResponse recommendRoadmap(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return new RoadmapRecommendationResponse();
    }

}
