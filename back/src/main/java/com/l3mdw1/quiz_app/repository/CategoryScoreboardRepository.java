package com.l3mdw1.quiz_app.repository;

import com.l3mdw1.quiz_app.model.CategoryScoreboard;
import com.l3mdw1.quiz_app.model.CategoryScoreboardId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CategoryScoreboardRepository extends JpaRepository<CategoryScoreboard, CategoryScoreboardId> {
    List<CategoryScoreboard> findByCategoryCatScoreId(Long categoryId);
    List<CategoryScoreboard> findByUserCatScoreId(Long userId);
}