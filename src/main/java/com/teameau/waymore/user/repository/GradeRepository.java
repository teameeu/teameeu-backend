package com.teameau.waymore.user.repository;

import com.teameau.waymore.user.domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByUser_UserId(Long userId);
}
