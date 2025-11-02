package com.l3mdw1.quiz_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@Entity
public class QuestionQuiz {
    @EmbeddedId
    private QuestionQuizId id = new QuestionQuizId();

    @ManyToOne
    @MapsId("quizId")
    @JoinColumn(name = "quiz_id")
    @JsonIgnore
    private Quiz quiz;

    @ManyToOne
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    @JsonIgnore
    private Question question;

    @Column(nullable = false)
    private boolean answeredCorrectly;

    public QuestionQuiz() {}
    public QuestionQuiz(Quiz quiz, Question question, boolean answeredCorrectly) {
        this.quiz = quiz;
        this.question = question;
        this.answeredCorrectly = answeredCorrectly;
        this.id = new QuestionQuizId(quiz.getId(), question.getId());
    }
}
