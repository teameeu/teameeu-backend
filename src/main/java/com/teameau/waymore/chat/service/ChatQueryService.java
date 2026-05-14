package com.teameau.waymore.chat.service;

import com.teameau.waymore.chat.domain.ChatSession;
import com.teameau.waymore.chat.dto.ChatMessageResponse;
import com.teameau.waymore.chat.dto.ChatRoomDetailResponse;
import com.teameau.waymore.chat.dto.ChatRoomListResponse;
import com.teameau.waymore.chat.dto.ChatRoomSummaryResponse;
import com.teameau.waymore.chat.repository.ChatMessageRepository;
import com.teameau.waymore.chat.repository.ChatSessionRepository;
import com.teameau.waymore.common.exception.BusinessException;
import com.teameau.waymore.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatQueryService {
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;


    // TODO: javadoc 상세 작성

    /**
     * 현재 사용자의 채팅방 목록 조회
     */
    public ChatRoomListResponse getChatRoom(Long userId) {
        List<ChatRoomSummaryResponse> chatRooms = chatSessionRepository.findAllByUser_UserIdOrderByLastMessageAtDescCreatedAtDesc(userId)
                .stream()
                .map(ChatRoomSummaryResponse::from)
                .toList();

        return new ChatRoomListResponse(chatRooms);
    }

    /**
     * 특정 채팅방 상새 조회
     */
    public ChatRoomDetailResponse getChatRoom(Long userId, Long sessionId) {
        ChatSession chatSession = chatSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST)); // TODO: 에러코드 제작

        validateOwnership(userId, chatSession);

        List<ChatMessageResponse> messages = chatMessageRepository.findAllByChatSession_SessionIdOrderByCreatedAtAsc(sessionId)
                .stream()
                .map(ChatMessageResponse::from)
                .toList();

        return ChatRoomDetailResponse.of(chatSession, messages);
    }

    /**
     * 사용자 검증
     */
    public void validateOwnership(Long userId, ChatSession chatSession) {
        if (!chatSession.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST); // TODO: 에러코드 변경
        }
    }


}
