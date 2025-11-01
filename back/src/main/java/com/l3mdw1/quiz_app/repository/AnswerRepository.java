package com.l3mdw1.quiz_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.l3mdw1.quiz_app.model.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long>{
}