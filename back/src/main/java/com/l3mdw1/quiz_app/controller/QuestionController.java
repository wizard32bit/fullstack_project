package com.l3mdw1.quiz_app.controller;

import com.l3mdw1.quiz_app.model.Question;
import com.l3mdw1.quiz_app.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    // Get questions by quiz ID
    @GetMapping("/from/quiz/{quizId}")
    public ResponseEntity<?> getQuestionsByQuiz(@PathVariable Long quizId) {
        try {
            List<Question> questions = questionService.getQuestionsByQuizId(quizId);
            return ResponseEntity.ok(questions);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Get question by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestion(@PathVariable Long id) {
        try {
            Question question = questionService.getQuestion(id);
            return ResponseEntity.ok(question);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Get all questions
    @GetMapping("/")
    public ResponseEntity<?> getAllQuestions() {
        try {
            List<Question> questions = questionService.getAll();
            return ResponseEntity.ok(questions);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    // Create question by quiz ID
    @PostMapping("/new/in/quiz/{quizId}")
    public ResponseEntity<?> createQuestionByQuizId(
            @PathVariable Long quizId,
            @RequestBody Question question) {
        try {
            Question savedQuestion = questionService.createQuestionByQuizId(quizId, question);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestion);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    // Update question + optionally change quiz
    @PutMapping("/put/{id}/to/quiz/{quizId}")
    public ResponseEntity<?> updateQuestionQuiz(
            @PathVariable Long id,
            @PathVariable Long quizId,
            @RequestBody Question updatedQuestion) {
        try {
            Question updated = questionService.updateQuestionQuiz(id, updatedQuestion, quizId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Update question normally
    @PutMapping("/put/{id}")
    public ResponseEntity<?> updateQuestion(
            @PathVariable Long id,
            @RequestBody Question updatedQuestion) {
        try {
            Question updated = questionService.updateQuestion(id, updatedQuestion);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    // Delete question
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        try {
            questionService.deleteQuestion(id);
            return ResponseEntity.ok("Question supprimée avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
