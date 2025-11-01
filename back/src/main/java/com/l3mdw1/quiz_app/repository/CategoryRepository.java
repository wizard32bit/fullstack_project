package com.l3mdw1.quiz_app.repository;

import com.l3mdw1.quiz_app.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
