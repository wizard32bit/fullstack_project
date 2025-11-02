package com.l3mdw1.quiz_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String content;

    @Column(nullable = false, updatable = false)
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name= "question_id")
    @JsonIgnore
    private Question question;

    public Answer(String content, boolean isCorrect){
        this.isCorrect= isCorrect;
        this.content= content;
    }

    public Answer(String content, boolean correct, Question q){
        this.content= content;
        this.isCorrect= correct;
        this.question= q;
    }
}


/*
* [------ Answer -------]           [-------- Question -------]
* | id: Long            |           | id: Long                |
* | content: String     |_________/\| id: Long                |
* |is_correct: boolean  |         \/| content: String         |
* |_____________________|           |_________________________|
 */
