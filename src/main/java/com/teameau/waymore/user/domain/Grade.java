package com.teameau.waymore.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

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
    private String score;

    /**
     * 등급
     */
    @Column(name = "grade", nullable = false)
    private String grade;


    @Builder
    private Grade(User user) {
        this.user = user;
    }

}
