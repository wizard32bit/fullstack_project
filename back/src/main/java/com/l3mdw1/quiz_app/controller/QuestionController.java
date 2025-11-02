package com.l3mdw1.quiz_app.controller;

import com.l3mdw1.quiz_app.model.Category;
import com.l3mdw1.quiz_app.model.Question;
import com.l3mdw1.quiz_app.repository.CategoryRepository;
import com.l3mdw1.quiz_app.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getQuestionsByCategory(@PathVariable Long categoryId) {
        try {
            List<Question> questions = questionService.getQuestionsByCategory(categoryId);
            return ResponseEntity.ok(questions);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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

    @PostMapping("/new/category/{catId}")
    public ResponseEntity<?> createQuestion(@RequestBody Question question, @PathVariable Long catId) {
        try {
            Question savedQuestion = questionService.createQuestion(question, catId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestion);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Update question normally
    @PutMapping("/{id}/category/{catId}")
    public ResponseEntity<?> updateQuestion(
            @PathVariable Long id,
            @PathVariable Long catId,
            @RequestBody Question updatedQuestion) {
        try {
            Question updated = questionService.updateQuestion(id, updatedQuestion, catId);
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
