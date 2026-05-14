package com.teameau.waymore.chat.service;

import com.teameau.waymore.chat.client.AiChatSseClient;
import com.teameau.waymore.chat.client.AiChatStreamEvent;
import com.teameau.waymore.chat.client.AiChatStreamRequest;
import com.teameau.waymore.chat.domain.ChatMessage;
import com.teameau.waymore.chat.domain.ChatMessageRole;
import com.teameau.waymore.chat.domain.ChatSession;
import com.teameau.waymore.chat.dto.*;
import com.teameau.waymore.chat.repository.ChatMessageRepository;
import com.teameau.waymore.chat.repository.ChatSessionRepository;
import com.teameau.waymore.common.exception.BusinessException;
import com.teameau.waymore.common.exception.ErrorCode;
import com.teameau.waymore.user.domain.User;
import com.teameau.waymore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;



@Service
@RequiredArgsConstructor
public class ChatCommandService {
    private final UserRepository userRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final AiChatSseClient aiChatSseClient;
    private final SimpMessagingTemplate messagingTemplate;

    // TODO: javadoc 작성하기


    /**
     * 사용자 메세지 저장 및 AI 서버 서트림 구독, STOMP 중계
     */
    @Transactional
    public void sendMessage(Long userId, ChatSendRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        ChatSession chatSession = resolveChatRoom(user, request.sessionId(), request.content());
        LocalDateTime now = LocalDateTime.now();

        ChatMessage userMessage = chatMessageRepository.save(
                ChatMessage.builder()
                .chatSession(chatSession)
                .role(ChatMessageRole.USER)
                .content(request.content())
                .build());

        ChatMessage assistantMessage = chatMessageRepository.save(
                ChatMessage.builder()
                    .chatSession(chatSession)
                    .role(ChatMessageRole.AI)
                    .content("")
                    .build()
        );


        chatSession.updateWithMessage(request.content(), now);

        publish(chatSession.getSessionId(), ChatSocketEventResponse.ack(
                chatSession.getSessionId(),
                userMessage.getMessageId(),
                assistantMessage.getMessageId()
        ));


        publish(chatSession.getSessionId(), ChatSocketEventResponse.start(
                chatSession.getSessionId(),
                assistantMessage.getMessageId()
        ));


        List<ChatMessageResponse> history = chatMessageRepository.findTop20ByChatSession_SessionIdOrderByCreatedAtDesc(chatSession.getSessionId())
                .stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedAt))
                .map(ChatMessageResponse::from)
                .toList();

        StringBuilder buffer = new StringBuilder();

        aiChatSseClient.stream(new AiChatStreamRequest(
                userId,
                chatSession.getSessionId(),
                userMessage.getMessageId(),
                request.content(),
                history
        ))
                .doOnNext(event -> handleStreamEvent(chatSession, assistantMessage, buffer, event))
                .doOnError(error -> handleError(chatSession, assistantMessage, buffer, error.getMessage()))
                .doOnComplete(() -> finalizeMessage(chatSession, assistantMessage, buffer.toString()))
                .subscribe();
    }


    /**
     * 기존 채티방 조회 및 신규 채팅방 생성
     * @param user
     * @param sessionId
     * @param content
     * @return
     */
    private ChatSession resolveChatRoom(User user, Long sessionId, String content) {
        if (sessionId == null) {
            return chatSessionRepository.save(ChatSession.builder()
                    .user(user)
                    .title(makeTitle(content))
                    .build());
        }

        ChatSession chatRoom = chatSessionRepository.findBySessionId(sessionId).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));  // TODO: 에러코드 만들기

        if (!chatRoom.getUser().getUserId().equals(user.getUserId())) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        return chatRoom;
    }

    /**
     * 채팅방 제목 생성(첫 메세지로 생성)
     * @param content 사용자 메세지
     * @return
     */
    private String makeTitle(String content) {
        if (content.length() <= 20) {
            return content;
        }

        return content.substring(0,20);
    }

    /**
     * SSE 이벤트 청크 및 오류 이벤트 중계
     */
    private void handleStreamEvent(ChatSession chatRoom, ChatMessage assistantMessage, StringBuilder buffer, AiChatStreamEvent event) {
        if (event.isError()) {
            handleError(chatRoom, assistantMessage, buffer, event.errorMessage());
            return;
        }

        if (event.isChunk() || event.isDone()) {
            String content = event.content();
            if (content != null) {
                buffer.append(content);
                publish(chatRoom.getSessionId(), ChatSocketEventResponse.chunk(
                        chatRoom.getSessionId(),
                        assistantMessage.getMessageId(),
                        content
                ));
            }
        }

    }


    /**
     * AI 스트리밍 종료 이후, 메시지 저장 및 완료 이벤트 처리
     */

    @Transactional
    protected void finalizeMessage(ChatSession chatRoom, ChatMessage assistantMessage, String content) {
        assistantMessage.updateContent(content);
        chatRoom.updateWithMessage(content, LocalDateTime.now());

        publish(chatRoom.getSessionId(), ChatSocketEventResponse.done(
                chatRoom.getSessionId(),
                assistantMessage.getMessageId(),
                content
        ));
    }


    /**
     * 에러 핸들러
     * - AI 처리 중 오류 발생하면 누적된 내용만 저장 및 오류 이벤트 전송
     */
    @Transactional
    protected void handleError(ChatSession chatRoom, ChatMessage assistantMessage, StringBuilder buffer, String errorMessage) {
        assistantMessage.updateContent(buffer.toString());
        publish(chatRoom.getSessionId(), ChatSocketEventResponse.error(
                chatRoom.getSessionId(),
                assistantMessage.getMessageId(),
                errorMessage == null ? "AI 처리 중 오류가 발생했습니다. " : errorMessage
        ));
    }

    /**
     * 이벤트 발생
     */
    private void publish(Long sessionId, ChatSocketEventResponse payload) {
        messagingTemplate.convertAndSend("/sub/chats/" + sessionId, payload);
    }



}
