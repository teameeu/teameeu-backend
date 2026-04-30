package com.teameau.waymore.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "\"USERS\"")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    /*
    * 회원 PK
    * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    /*
    * 이메일
    * */
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    /*
    * 비밀번호
    * */
    @Column(name = "password", nullable = false, length = 255)
    private String password;



    /*
    * 생년월일*/
    @Column(name= "birthday", nullable = false)
    private LocalDate birthday;


    /*
    * 생성시각*/
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /*
    * 희망학과*/
    @Column(name = "department", nullable = false, length = 20)
    private String department;

    /*
    * 희망진로*/
    @Column(name = "career", nullable = false, length = 20)
    private String career;

    /*
    * 회원 권한
    * TODO: 추후 ROLE 도입시 사용
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
     */

    @Builder
    private User(
            String email,
            String password,
            LocalDate birthday,
            String department,
            String career
    ) {
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.department = department;
        this.career = career;
    }

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();

        // TODO: 추후 ROLE 도입 시, 기본 ROLE은 USER로 등록
    }
}
