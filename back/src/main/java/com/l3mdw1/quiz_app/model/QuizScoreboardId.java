package com.l3mdw1.quiz_app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class QuizScoreboardId implements Serializable {
    private Long quizId;
    private Long userId;

    public QuizScoreboardId() {}

    public QuizScoreboardId(Long quizId, Long userId) {
        this.quizId = quizId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuizScoreboardId)) return false;
        QuizScoreboardId that = (QuizScoreboardId) o;
        return Objects.equals(quizId, that.quizId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quizId, userId);
    }
}
