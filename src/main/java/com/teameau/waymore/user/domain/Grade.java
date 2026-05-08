package com.teameau.waymore.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "\"GRADE\"")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Grade {


    /**
     * 성적 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private Long gradeId;

    /**
     * 사용자 id(FK)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 과목명
     */
    @Column(name = "subject", nullable = false)
    private String subject;

    /**
     * 성적
     */
    @Column(name = "score", nullable = false)
    private int score;

    /**
     * 등급
     */
    @Column(name = "grade", nullable = false)
    private String grade;


    @Builder
    private Grade(
            User user,
            String subject,
            int score,
            String grade
    ) {
        this.user = user;
        this.subject = subject;
        this.score = score;
        this.grade = grade;
    }

    public void update(String subject, int score, String grade) {
        this.subject = subject;
        this.score = score;
        this.grade = grade;
    }
}
