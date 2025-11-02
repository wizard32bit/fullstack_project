package com.l3mdw1.quiz_app.controller;

import com.l3mdw1.quiz_app.model.QuestionQuiz;
import com.l3mdw1.quiz_app.service.QuestionQuizService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/question-quiz")
public class QuestionQuizController {

    @Autowired
    private QuestionQuizService questionQuizService;

    @PostMapping("/assign/{questionId}/to/{quizId}")
    public ResponseEntity<?> assignQuestionToQuiz(
            @PathVariable Long quizId,
            @PathVariable Long questionId,
            @RequestBody QuestionQuiz questionQuiz) {
        try {
            QuestionQuiz saved = questionQuizService.assignQuestionToQuiz(quizId, questionId, questionQuiz);
            return ResponseEntity.ok(saved);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<?> getQuestionQuizByQuizId(Long quizId){
        try{
            return ResponseEntity.ok(questionQuizService.getQuestionQuizByQuizId(quizId));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}