package com.l3mdw1.quiz_app.controller;

import com.l3mdw1.quiz_app.model.CategoryScoreboard;
import com.l3mdw1.quiz_app.service.CategoryScoreboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category-scores")
public class CategoryScoreboardController {

    @Autowired
    private CategoryScoreboardService categoryScoreboardService;

    @GetMapping
    public ResponseEntity<?> getAllCategoryScores() {
        try {
            List<CategoryScoreboard> scores = categoryScoreboardService.getAll();
            return ResponseEntity.ok(scores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getScoresByUser(@PathVariable Long userId) {
        try {
            List<CategoryScoreboard> scores = categoryScoreboardService.getScoresByUser(userId);
            return ResponseEntity.ok(scores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getScoresByCategory(@PathVariable Long categoryId) {
        try {
            List<CategoryScoreboard> scores = categoryScoreboardService.getScoresByCategory(categoryId);
            return ResponseEntity.ok(scores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{categoryId}/{userId}")
    public ResponseEntity<?> getScore(@PathVariable Long categoryId, @PathVariable Long userId) {
        try {
            CategoryScoreboard score = categoryScoreboardService.getScore(userId, categoryId);
            return ResponseEntity.ok(score);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> saveScore(@RequestBody CategoryScoreboard categoryScoreboard) {
        try {
            CategoryScoreboard saved = categoryScoreboardService.saveScore(categoryScoreboard);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{categoryId}/{userId}")
    public ResponseEntity<?> deleteScore(@PathVariable Long categoryId, @PathVariable Long userId) {
        try {
            categoryScoreboardService.deleteScore(userId, categoryId);
            return ResponseEntity.ok("Score supprimé avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
