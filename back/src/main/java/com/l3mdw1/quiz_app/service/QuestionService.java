package com.l3mdw1.quiz_app.service;

import com.l3mdw1.quiz_app.model.Answer;
import com.l3mdw1.quiz_app.model.Category;
import com.l3mdw1.quiz_app.model.Question;
import com.l3mdw1.quiz_app.repository.CategoryRepository;
import com.l3mdw1.quiz_app.repository.QuestionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    public List<Question> getAll() {
        List<Question> questions = questionRepo.findAll();
        if (questions.isEmpty()) {
            throw new RuntimeException("Aucune question enregistrée dans la base de données");
        }
        return questions;
    }

    public Question getQuestion(Long id) {
        return questionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Aucune question avec cet id: " + id));
    }

    public List<Question> getQuestionsByCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId));

        List<Question> questions = category.getQuestions();

        if (questions.isEmpty()) {
            throw new RuntimeException("No questions found in this category");
        }

        return questions;
    }

    @Transactional
    public Question createQuestion(Question question, Long catId) {
        if (question.getContent() == null || question.getContent().trim().isEmpty()) {
            throw new RuntimeException("Le contenu de la question ne peut pas être vide");
        }
        question.setContent(question.getContent().trim());

        // Validate answers
        if (question.getAnswers() == null || question.getAnswers().isEmpty()) {
            throw new RuntimeException("La question doit avoir des réponses");
        }

        long correctCount = 0;
        for (Answer answer : question.getAnswers()) {
            if (answer.getContent() == null || answer.getContent().trim().isEmpty()) {
                throw new RuntimeException("Chaque réponse doit avoir un contenu");
            }
            answer.setContent(answer.getContent().trim());
            answer.setQuestion(question);
            if (answer.isCorrect()) correctCount++;
        }

        if (correctCount != 1) {
            throw new RuntimeException("Une question doit avoir exactement une seule réponse correcte");
        }

        if (question.getAnswers().size() > 4) {
            throw new RuntimeException("Une question doit avoir au plus 4 réponses");
        }

        if(catId != null) {
            Category category = categoryRepo.findById(catId)
                    .orElseThrow(() -> new RuntimeException("Catégorie introuvable avec l'id: " + catId));
            question.setCategory(category);
        }
        return questionRepo.save(question);
    }

    @Transactional
    public Question updateQuestion(Long id, Question updatedQuestion, Long catId) {
        Question questionFromDB = getQuestion(id);

        // Update category if provided
        if(catId != null) {
            Category category = categoryRepo.findById(catId)
                    .orElseThrow(() -> new RuntimeException("Catégorie introuvable avec l'id: " + catId));
            questionFromDB.setCategory(category);
        }
        // Update content
        if (updatedQuestion.getContent() != null && !updatedQuestion.getContent().trim().isEmpty()) {
            questionFromDB.setContent(updatedQuestion.getContent().trim());
        }

        // Update answers
        if (updatedQuestion.getAnswers() != null && !updatedQuestion.getAnswers().isEmpty()) {
            long correctCount = updatedQuestion.getAnswers().stream().filter(Answer::isCorrect).count();
            if (correctCount != 1) {
                throw new RuntimeException("Une question doit avoir exactement une seule réponse correcte");
            }
            if (updatedQuestion.getAnswers().size() > 4) {
                throw new RuntimeException("Une question doit avoir au plus 4 réponses");
            }

            // Update or add answers
            for (Answer updatedAnswer : updatedQuestion.getAnswers()) {
                if (updatedAnswer.getId() != null) {
                    Answer existing = questionFromDB.getAnswers().stream()
                            .filter(a -> a.getId().equals(updatedAnswer.getId()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Réponse introuvable: " + updatedAnswer.getId()));
                    existing.setContent(updatedAnswer.getContent().trim());
                    existing.setCorrect(updatedAnswer.isCorrect());
                } else {
                    updatedAnswer.setQuestion(questionFromDB);
                    questionFromDB.getAnswers().add(updatedAnswer);
                }
            }
        }

        return questionRepo.save(questionFromDB);
    }



    public void deleteQuestion(Long id) {
        Question question = getQuestion(id);
        questionRepo.delete(question);
    }

}
