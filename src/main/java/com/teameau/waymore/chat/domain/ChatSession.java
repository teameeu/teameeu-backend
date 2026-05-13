package com.teameau.waymore.chat.domain;

import com.teameau.waymore.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name ="\"CHAT_SESSION\"" )
public class ChatSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", length = 50)
    private String title;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;

    @OneToMany(mappedBy = "chatSession")
    private List<ChatMessage> messages;

    @Builder
    private ChatSession(User user, String title) {
        this.user = user;
        this.title = title;
    }

    /**
     * 마지막 메세지 전송시간 업데이트
     * @param content 마지막 메세지 내용
     * @param sentAt 마지막 메세지 시각
     */
    public void updateWithMessage(String content, LocalDateTime sentAt) {
        this.lastMessageAt = sentAt;
    }

    /**
     * 생성 시각 자동 설정
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }





}
