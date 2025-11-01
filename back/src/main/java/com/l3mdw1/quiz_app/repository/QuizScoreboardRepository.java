package com.l3mdw1.quiz_app.repository;


import com.l3mdw1.quiz_app.model.QuizScoreboard;
import com.l3mdw1.quiz_app.model.QuizScoreboardId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizScoreboardRepository extends JpaRepository<QuizScoreboard, QuizScoreboardId> {
    List<QuizScoreboard> findByQuizId(Long quizId);
    List<QuizScoreboard> findByUserId(Long userId);
}