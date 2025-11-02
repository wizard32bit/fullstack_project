package com.l3mdw1.quiz_app.controller;

import com.l3mdw1.quiz_app.model.CategoryScoreboard;
import com.l3mdw1.quiz_app.service.CategoryScoreboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.event.RepositoryEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category-scores")
public class CategoryScoreboardController {

    @Autowired
    private CategoryScoreboardService categoryScoreboardService;

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

    @GetMapping("/")
    public ResponseEntity<?> getAllCategoryScores() {
        try {
            List<CategoryScoreboard> scores = categoryScoreboardService.getAll();
            return ResponseEntity.ok(scores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/assign-score/{userId}/{catId}")
    public ResponseEntity<?> saveScore(@PathVariable Long userId,
                                       @PathVariable Long catId,
                                       @RequestBody CategoryScoreboard categoryScoreboard) {
        try {
            CategoryScoreboard saved = categoryScoreboardService.saveScore(userId, catId, categoryScoreboard);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateScore(@PathVariable Long userId,
                                         @PathVariable Long catId,
                                         @RequestBody CategoryScoreboard updatedCategoryScoreboard) {
        try{
            CategoryScoreboard updatedscore= categoryScoreboardService.updateScore(userId, catId, updatedCategoryScoreboard);
            return ResponseEntity.ok(updatedscore);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/{categoryId}")
    public ResponseEntity<?> deleteScore(@PathVariable Long categoryId, @PathVariable Long userId) {
        try {
            categoryScoreboardService.deleteScore(userId, categoryId);
            return ResponseEntity.ok("Score supprimé avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
