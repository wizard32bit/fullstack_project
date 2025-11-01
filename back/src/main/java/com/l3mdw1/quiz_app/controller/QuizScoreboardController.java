package com.l3mdw1.quiz_app.controller;

import com.l3mdw1.quiz_app.model.QuizScoreboard;
import com.l3mdw1.quiz_app.service.QuizScoreboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-scores")
public class QuizScoreboardController {

    @Autowired
    private QuizScoreboardService quizScoreboardService;

    // Create or update a score
    @PostMapping("/assign")
    public ResponseEntity<?> assignScore(
            @RequestParam Long quizId,
            @RequestParam Long userId,
            @RequestParam int passedQs,
            @RequestParam int failedQs,
            @RequestParam int score) {
        try {
            QuizScoreboard savedScore = quizScoreboardService.assignScore(quizId, userId, passedQs, failedQs, score);
            return ResponseEntity.ok(savedScore);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne du serveur : " + e.getMessage());
        }
    }

    // Get all scores for a quiz
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<?> getScoresByQuiz(@PathVariable Long quizId) {
        try {
            List<QuizScoreboard> scores = quizScoreboardService.getScoresByQuiz(quizId);
            if (scores.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Aucun score trouvé pour le quiz avec l'id : " + quizId);
            }
            return ResponseEntity.ok(scores);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne du serveur : " + e.getMessage());
        }
    }

    // Get all scores for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getScoresByUser(@PathVariable Long userId) {
        try {
            List<QuizScoreboard> scores = quizScoreboardService.getScoresByUser(userId);
            if (scores.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Aucun score trouvé pour l'utilisateur avec l'id : " + userId);
            }
            return ResponseEntity.ok(scores);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne du serveur : " + e.getMessage());
        }
    }

    // Delete a score
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteScore(
            @RequestParam Long quizId,
            @RequestParam Long userId) {
        try {
            quizScoreboardService.deleteScore(quizId, userId);
            return ResponseEntity.ok("Score supprimé avec succès pour le quiz " + quizId + " et l'utilisateur " + userId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne du serveur : " + e.getMessage());
        }
    }
}
