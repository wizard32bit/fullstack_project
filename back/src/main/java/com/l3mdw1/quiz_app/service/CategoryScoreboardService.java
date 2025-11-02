package com.l3mdw1.quiz_app.service;

import com.l3mdw1.quiz_app.model.Category;
import com.l3mdw1.quiz_app.model.CategoryScoreboard;
import com.l3mdw1.quiz_app.model.CategoryScoreboardId;
import com.l3mdw1.quiz_app.model.User;
import com.l3mdw1.quiz_app.repository.CategoryRepository;
import com.l3mdw1.quiz_app.repository.CategoryScoreboardRepository;
import com.l3mdw1.quiz_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryScoreboardService {

    @Autowired
    private CategoryScoreboardRepository categoryScoreboardRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CategoryRepository categoryRepo;


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


    public List<CategoryScoreboard> getScoresByUser(Long userId) {
        return categoryScoreboardRepo.findByUserCatScoreId(userId);
    }

    public List<CategoryScoreboard> getScoresByCategory(Long categoryId) {
        return categoryScoreboardRepo.findByCategoryCatScoreId(categoryId);
    }

    @Transactional
    public CategoryScoreboard saveScore(Long userId, Long catId, CategoryScoreboard categoryScoreboard) {
        if (categoryScoreboard == null) {
            throw new RuntimeException("Aucun score à enregistrer");
        }

        // Validate score range (optional but recommended)
        if (categoryScoreboard.getScore() < 0 || categoryScoreboard.getScore() > 100) {
            throw new RuntimeException("Le score doit être compris entre 0 et 100");
        }

        // Fetch user and category
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Aucun utilisateur avec cet id: " + userId));

        Category category = categoryRepo.findById(catId)
                .orElseThrow(() -> new RuntimeException("Aucune catégorie avec cet id: " + catId));

        CategoryScoreboardId id = new CategoryScoreboardId(catId, userId);

        // Check if score already exists for this user/category
        if (categoryScoreboardRepo.existsById(id)) {
            throw new RuntimeException("Le score pour cet utilisateur et cette catégorie existe déjà. Utilisez la mise à jour plutôt.");
        }

        // Create and save new scoreboard
        CategoryScoreboard newScoreboard = new CategoryScoreboard(category, user, categoryScoreboard.getScore());
        return categoryScoreboardRepo.save(newScoreboard);
    }


    @Transactional
    public CategoryScoreboard updateScore(Long userId, Long catId, CategoryScoreboard updatedScore) {
        if (updatedScore == null) {
            throw new RuntimeException("Aucun score à enregistrer");
        }

        // Fetch user and category
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Aucun utilisateur avec cet id: " + userId));

        Category category = categoryRepo.findById(catId)
                .orElseThrow(() -> new RuntimeException("Aucune catégorie avec cet id: " + catId));

        // Validate score range (optional)
        if (updatedScore.getScore() < 0 || updatedScore.getScore() > 100) {
            throw new RuntimeException("Le score doit être compris entre 0 et 100");
        }

        // Fetch existing record
        CategoryScoreboardId id = new CategoryScoreboardId(category.getId(), user.getId());
        CategoryScoreboard existing = categoryScoreboardRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Aucun score trouvé pour cet utilisateur et cette catégorie"));

        // Update the score
        existing.setScore(updatedScore.getScore());

        return categoryScoreboardRepo.save(existing);
    }


    public void deleteScore(Long userId, Long categoryId) {
        CategoryScoreboardId id = new CategoryScoreboardId(categoryId, userId);
        CategoryScoreboard existing = getScore(userId, categoryId);
        categoryScoreboardRepo.delete(existing);
    }

}
