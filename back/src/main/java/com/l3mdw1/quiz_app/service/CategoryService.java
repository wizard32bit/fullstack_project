package com.l3mdw1.quiz_app.service;

import com.l3mdw1.quiz_app.model.Category;
import com.l3mdw1.quiz_app.model.CategoryScoreboard;
import com.l3mdw1.quiz_app.model.User;
import com.l3mdw1.quiz_app.repository.CategoryRepository;
import com.l3mdw1.quiz_app.repository.CategoryScoreboardRepository;
import com.l3mdw1.quiz_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CategoryScoreboardRepository scoreboardRepo;



    // Get all categories
    public List<Category> getAllCategories() {
        List<Category> categories= categoryRepo.findAll();
        if(categories.isEmpty()){
            throw new RuntimeException("Aucun catégorie trouvé");
        }
        return categories;
    }

    // Get category by id
    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Aucune categorie avec l'id: " + id));
    }

    // Create a new category
    public Category createCategory(Category category) {
        if(category.getName() == null || category.getName().trim().isEmpty()){
            throw new RuntimeException("Nom categorie invalide");
        }
        return categoryRepo.save(category);
    }

    // Update category
    public Category updateCategory(Long id, Category updatedCategory) {
        Category existingCategory = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Aucune catégorie trouvée avec l'id : " + id));

        if(updatedCategory.getName() == null || updatedCategory.getName().trim().isEmpty()){
            throw new RuntimeException("Nom categorie invalide");
        }
        existingCategory.setName(updatedCategory.getName());
        return categoryRepo.save(existingCategory);
    }

    // Delete category
    public void deleteCategory(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Aucun categorie avec l'id: " + id));
        categoryRepo.delete(category);
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
