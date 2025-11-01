package com.l3mdw1.quiz_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.l3mdw1.quiz_app.model.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long>{
    List<Question> getQuestionsByQuizId(Long quizId);
}
