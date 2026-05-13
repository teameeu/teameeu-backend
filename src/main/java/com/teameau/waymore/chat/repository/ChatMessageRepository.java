package com.teameau.waymore.chat.repository;

import com.teameau.waymore.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * 채팅 메시지 조회 및 저장
 */
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByChatSession_SessionIdOrderByCreatedAtAsc(Long sessionId);

    // TODO: AI 요청에 최근 대화 히스토리까지 넘겨?
    List<ChatMessage> findTop20ByChatSession_SessionIdOrderByCreatedAtDesc(Long sessionId);



}
