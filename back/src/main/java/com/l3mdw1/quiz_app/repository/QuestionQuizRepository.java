package com.l3mdw1.quiz_app.repository;

import com.l3mdw1.quiz_app.model.QuestionQuiz;
import com.l3mdw1.quiz_app.model.QuestionQuizId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionQuizRepository extends JpaRepository<QuestionQuiz, QuestionQuizId> {
    List<QuestionQuiz> findByQuizId(Long quizId);
}