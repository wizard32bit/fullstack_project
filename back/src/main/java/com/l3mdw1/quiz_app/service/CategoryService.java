package com.l3mdw1.quiz_app.service;

import com.l3mdw1.quiz_app.model.Category;
import com.l3mdw1.quiz_app.model.CategoryScoreboard;
import com.l3mdw1.quiz_app.model.User;
import com.l3mdw1.quiz_app.repository.CategoryRepository;
import com.l3mdw1.quiz_app.repository.CategoryScoreboardRepository;
import com.l3mdw1.quiz_app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;
    private final CategoryScoreboardRepository scoreboardRepo;

    public CategoryService(CategoryRepository categoryRepo, UserRepository userRepo, CategoryScoreboardRepository scoreboardRepo) {
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
        this.scoreboardRepo = scoreboardRepo;
    }

    // Create a new category
    public Category createCategory(Category category) {
        return categoryRepo.save(category);
    }

    // Update category
    public Category updateCategory(Long id, String name) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        category.setName(name);
        return categoryRepo.save(category);
    }

    // Delete category
    public void deleteCategory(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoryg not found with id: " + id));
        categoryRepo.delete(category);
    }

    // Get all categories
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    // Get category by id
    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    // Assign or update user score for a category
    public CategoryScoreboard assignScore(Long categoryId, Long userId, int score) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Check if score already exists
        CategoryScoreboard scoreboard = scoreboardRepo.findById(new com.l3mdw1.quiz_app.model.CategoryScoreboardId(categoryId, userId))
                .orElse(new CategoryScoreboard(category, user, score));

        scoreboard.setScore(score);
        return scoreboardRepo.save(scoreboard);
    }

    // Get all scores for a category
    public List<CategoryScoreboard> getCategoryScores(Long categoryId) {
        return scoreboardRepo.findByCategoryCatScoreId(categoryId);
    }

    // Get all scores for a user
    public List<CategoryScoreboard> getUserScores(Long userId) {
        return scoreboardRepo.findByUserCatScoreId(userId);
    }
}
