package com.l3mdw1.quiz_app.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;

import java.io.Serializable;

@Embeddable
public class QuestionQuizId implements Serializable {
    private Long quizId;
    private Long questionId;

    public QuestionQuizId() {}
    public QuestionQuizId(Long quizId, Long questionId) {
        this.quizId = quizId;
        this.questionId = questionId;
    }

}
