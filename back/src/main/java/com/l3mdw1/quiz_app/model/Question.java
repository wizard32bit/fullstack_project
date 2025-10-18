package com.l3mdw1.quiz_app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name="quiz_id")
    @JsonIgnore
    private Quiz quiz;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers= new ArrayList<>();
}
/*
* [-------- Question -------]          |------ Answer -------|
* | id: Long                |/\_________|
* | id: Long                |\/         | content: String     |
* | content: String         |           |is_correct: boolean  |
* |_________________________|           |_____________________|
*           | *
*           |
*           | 1
* [-------- Quiz    --------]
* | id: Long                |
* [_________________________|
*
*/