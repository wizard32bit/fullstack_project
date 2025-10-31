package com.l3mdw1.quiz_app.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private boolean isCorrect;
}


/*
* [------ Answer -------]           [-------- Question -------]
* | id: Long            |           | id: Long                |
* | content: String     |_________/\| id: Long                |
* |is_correct: boolean  |         \/| content: String         |
* |_____________________|           |_________________________|
 */
