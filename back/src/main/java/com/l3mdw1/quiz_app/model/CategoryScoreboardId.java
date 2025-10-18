package com.l3mdw1.quiz_app.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CategoryScoreboardId implements Serializable {
    private Long catId;
    private Long userId;

    public CategoryScoreboardId() {}

    public CategoryScoreboardId(Long catId, Long userId) {
        this.catId = catId;
        this.userId = userId;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryScoreboardId)) return false;
        CategoryScoreboardId that = (CategoryScoreboardId) o;
        return Objects.equals(catId, that.catId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catId, userId);
    }
}
