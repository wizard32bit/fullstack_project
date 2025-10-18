package com.l3mdw1.quiz_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class CategoryScoreboard {

    @EmbeddedId
    private CategoryScoreboardId id= new CategoryScoreboardId();

    private int score;

    @MapsId("catId")
    @ManyToOne
    @JoinColumn(name = "cat_id")
    @JsonIgnore
    private Category categoryCatScore;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User userCatScore;

    public CategoryScoreboard() {}

    public CategoryScoreboard(Category category, User user, int score) {
        this.id = new CategoryScoreboardId(category.getId(), user.getId());
        this.categoryCatScore = category;
        this.userCatScore = user;
        this.score = score;
    }

}
