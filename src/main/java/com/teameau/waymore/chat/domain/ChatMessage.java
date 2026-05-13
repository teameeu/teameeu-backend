package com.teameau.waymore.chat.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "\"CHAT_SESSION\"" )
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ChatSession chatSession;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private ChatMessageRole role;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    private ChatMessage(
            ChatSession chatSession,
            ChatMessageRole chatMessageRole,
            String content
    ) {
        this.chatSession = chatSession;
        this.role = role;
        this.content = content;
    }

    /**
     * AI서버 SSE 이후, 최종 응답으로 갱신
     * @param content 최종 메세지 내용
     */
    public void updateContent(String content) {
        this.content = content;
    }

    /**
     * 생성 시각 입력
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }





}
