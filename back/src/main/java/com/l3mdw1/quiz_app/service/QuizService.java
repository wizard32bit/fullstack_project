package com.l3mdw1.quiz_app.service;

import com.l3mdw1.quiz_app.model.Category;
import com.l3mdw1.quiz_app.model.Question;
import com.l3mdw1.quiz_app.model.Quiz;
import com.l3mdw1.quiz_app.repository.CategoryRepository;
import com.l3mdw1.quiz_app.repository.QuestionRepository;
import com.l3mdw1.quiz_app.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private QuestionRepository questionRepo;

    public List<Quiz> getAll() {
        List<Quiz> quizzes = quizRepo.findAll();
        if (quizzes.isEmpty()) {
            throw new RuntimeException("Aucun quiz enregistré dans la base de données");
        }
        return quizzes;
    }

    public Quiz getQuiz(Long id) {
        return quizRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Aucun quiz trouvé avec l'id: " + id));
    }

    @Transactional
    public Quiz createQuiz(Quiz quiz) {
        if (quiz.getCategory() != null && quiz.getCategory().getId() != null) {
            Category category = categoryRepo.findById(quiz.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Aucune catégorie trouvée avec l'id: " + quiz.getCategory().getId()));
            quiz.setCategory(category);
        } else {
            throw new RuntimeException("Le quiz doit appartenir à une catégorie valide");
        }

        if (quiz.getQuestions() != null) {
            for (Question question : quiz.getQuestions()) {
                question.setQuiz(quiz);
            }
        }

        return quizRepo.save(quiz);
    }

    @Transactional
    public Quiz updateQuiz(Long id, Quiz updatedQuiz) {
        Quiz quizFromDB = getQuiz(id);

        if (updatedQuiz.getTitle() != null && !updatedQuiz.getTitle().trim().isEmpty()) {
            quizFromDB.setTitle(updatedQuiz.getTitle());
        }

        if (updatedQuiz.getCategory() != null && updatedQuiz.getCategory().getId() != null) {
            Category category = categoryRepo.findById(updatedQuiz.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Aucune catégorie trouvée avec l'id: " + updatedQuiz.getCategory().getId()));
            quizFromDB.setCategory(category);
        }

        if (updatedQuiz.getQuestions() != null && !updatedQuiz.getQuestions().isEmpty()) {
            quizFromDB.getQuestions().clear();
            for (Question question : updatedQuiz.getQuestions()) {
                question.setQuiz(quizFromDB);
                quizFromDB.getQuestions().add(question);
            }
        }

        return quizRepo.save(quizFromDB);
    }

    public void deleteQuiz(Long id) {
        Quiz quiz = getQuiz(id);
        quizRepo.delete(quiz);
    }

    public List<Question> getQuestionsByQuizId(Long quizId) {
        Quiz quiz = getQuiz(quizId);
        return quiz.getQuestions();
    }
}
