package com.l3mdw1.quiz_app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    private boolean isAdmin;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizScoreboard> quizScores = new ArrayList<>();

    @OneToMany(mappedBy = "userCatScore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryScoreboard> categoryScores = new ArrayList<>();


    public User(String uname, String e, String p, boolean isAdmin){
        this.username= uname;
        this.email= e;
        this.password= p;
        this.isAdmin= isAdmin;
    }
}
