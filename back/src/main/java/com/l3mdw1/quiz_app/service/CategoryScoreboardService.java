package com.l3mdw1.quiz_app.service;

import com.l3mdw1.quiz_app.model.CategoryScoreboard;
import com.l3mdw1.quiz_app.model.CategoryScoreboardId;
import com.l3mdw1.quiz_app.repository.CategoryScoreboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryScoreboardService {

    @Autowired
    private CategoryScoreboardRepository categoryScoreboardRepo;

    public List<CategoryScoreboard> getAll() {
        List<CategoryScoreboard> scores = categoryScoreboardRepo.findAll();
        if (scores.isEmpty()) {
            throw new RuntimeException("Aucun score de catégorie trouvé");
        }
        return scores;
    }

    public CategoryScoreboard getScore(Long userId, Long categoryId) {
        CategoryScoreboardId id = new CategoryScoreboardId(categoryId, userId);
        return categoryScoreboardRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Aucun score trouvé pour cet utilisateur et cette catégorie"));
    }

    public CategoryScoreboard saveScore(CategoryScoreboard categoryScoreboard) {
        return categoryScoreboardRepo.save(categoryScoreboard);
    }

    public void deleteScore(Long userId, Long categoryId) {
        CategoryScoreboardId id = new CategoryScoreboardId(categoryId, userId);
        CategoryScoreboard existing = getScore(userId, categoryId);
        categoryScoreboardRepo.delete(existing);
    }

    public List<CategoryScoreboard> getScoresByUser(Long userId) {
        return categoryScoreboardRepo.findByUserCatScoreId(userId);
    }

    public List<CategoryScoreboard> getScoresByCategory(Long categoryId) {
        return categoryScoreboardRepo.findByCategoryCatScoreId(categoryId);
    }
}
