package com.l3mdw1.quiz_app.controller;

import com.l3mdw1.quiz_app.model.Answer;
import com.l3mdw1.quiz_app.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @GetMapping("/question/{questionId}")
    public ResponseEntity<?> getAnswersByQuestion(@PathVariable Long questionId) {
        try {
            return ResponseEntity.ok(answerService.getAllByQuestionId(questionId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAnswer(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(answerService.getAnswer(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(answerService.getAll());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PostMapping("/new/into/question/{questionId}")
    public ResponseEntity<?> createAnswer(
            @PathVariable Long questionId,
            @RequestBody Answer answerRequest) {
        try {
            Answer createdAnswer = answerService.createAnswer(questionId, answerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAnswer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/put/{answerId}/from/question/{questionId}")
    public ResponseEntity<?> updateAnswer(
            @PathVariable Long questionId,
            @PathVariable Long answerId,
            @RequestBody Answer updatedAnswer) {
        try {
            return ResponseEntity.ok(answerService.updateAnswer(questionId, answerId, updatedAnswer));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{answerId}/from/question/{questionId}")
    public ResponseEntity<?> deleteAnswer(
            @PathVariable Long questionId,
            @PathVariable Long answerId) {
        try {
            answerService.deleteAnswer(questionId, answerId);
            return ResponseEntity.ok("Réponse supprimée avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
