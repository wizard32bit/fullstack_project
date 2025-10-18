package com.l3mdw1.quiz_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "quiz_scoreboard")
public class QuizScoreboard {

    @EmbeddedId
    private QuizScoreboardId id = new QuizScoreboardId();

    @ManyToOne
    @MapsId("quizId")
    @JoinColumn(name = "quiz_id")
    @JsonIgnore
    private Quiz quiz;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private int passedQs;
    private int failedQs;
    private int score;

    public QuizScoreboard(Quiz quiz, User user, int passedQs, int failedQs, int score) {
        this.id = new QuizScoreboardId(quiz.getId(), user.getId());
        this.quiz = quiz;
        this.user = user;
        this.passedQs = passedQs;
        this.failedQs = failedQs;
        this.score = score;
    }
}
