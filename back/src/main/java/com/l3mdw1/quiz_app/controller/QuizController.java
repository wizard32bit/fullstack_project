package com.l3mdw1.quiz_app.controller;

import com.l3mdw1.quiz_app.model.Question;
import com.l3mdw1.quiz_app.model.Quiz;
import com.l3mdw1.quiz_app.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    // GET all quizzes
    @GetMapping("/")
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        List<Quiz> quizzes = quizService.getAll();
        return ResponseEntity.ok(quizzes);
    }

    // GET quiz by ID
    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        Quiz quiz = quizService.getQuiz(id);
        return ResponseEntity.ok(quiz);
    }

    // CREATE new quiz
    @PostMapping("/new/category/{catId}")
    public ResponseEntity<Quiz> createQuiz(@PathVariable Long catId, @RequestBody Quiz quiz) {
        Quiz savedQuiz = quizService.createQuiz(catId, quiz);
        return ResponseEntity.ok(savedQuiz);
    }

    // UPDATE existing quiz
    @PutMapping("/{id}/category/{catId}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long id, @PathVariable Long catId,@RequestBody Quiz updatedQuiz) {
        Quiz quiz = quizService.updateQuiz(id, catId, updatedQuiz);
        return ResponseEntity.ok(quiz);
    }

    // DELETE quiz
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.ok("Quiz supprimé avec succès (ID: " + id + ")");
    }

    // GET all questions for a given quiz
    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<Question>> getQuestionsByQuiz(@PathVariable Long quizId) {
        List<Question> questions = quizService.getQuestionsByQuizId(quizId);
        return ResponseEntity.ok(questions);
    }
}
