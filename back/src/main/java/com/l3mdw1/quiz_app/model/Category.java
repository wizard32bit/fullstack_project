package com.l3mdw1.quiz_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Quiz> quizzes= new ArrayList<>();

    @OneToMany(mappedBy = "categoryCatScore", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CategoryScoreboard> userScores= new ArrayList<>();
}
