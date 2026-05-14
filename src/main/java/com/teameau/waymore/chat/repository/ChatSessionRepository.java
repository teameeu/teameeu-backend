package com.teameau.waymore.chat.repository;


import com.teameau.waymore.chat.domain.ChatSession;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 채팅 세션 조회 및 저장
 */
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

    List<ChatSession> findAllByUser_UserIdOrderByLastMessageAtDescCreatedAtDesc(Long userId);

    @EntityGraph(attributePaths = {"user"})
    Optional<ChatSession> findBySessionId(Long sessionId);
}
