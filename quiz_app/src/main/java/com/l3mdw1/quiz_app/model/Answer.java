package com.l3mdw1.quiz_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private boolean is_correct;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

}

/*
* [------ Answer -------]           [-------- Question -------]
* | id: Long            |           | id: Long                |
* | content: String     |_________/\| id: Long                |
* |is_correct: boolean  |         \/| content: String         |
* |_____________________|           |_________________________|
 */
