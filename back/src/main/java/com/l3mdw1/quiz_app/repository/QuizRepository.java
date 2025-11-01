package com.l3mdw1.quiz_app.repository;

import com.l3mdw1.quiz_app.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
