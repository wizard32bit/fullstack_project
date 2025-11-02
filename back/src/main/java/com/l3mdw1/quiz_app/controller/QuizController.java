package com.l3mdw1.quiz_app.controller;

import com.l3mdw1.quiz_app.model.Question;
import com.l3mdw1.quiz_app.model.Quiz;
import com.l3mdw1.quiz_app.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;



    // GET all questions for a given quiz
    @GetMapping("/{quizId}/questions")
    public ResponseEntity<?> getQuestionsByQuiz(@PathVariable Long quizId) {
        try {
            List<Question> questions = quizService.getQuestionsByQuizId(quizId);
            return ResponseEntity.ok(questions);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // GET quiz by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizById(@PathVariable Long id) {
        try {
            Quiz quiz = quizService.getQuiz(id);
            return ResponseEntity.ok(quiz);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // GET all quizzes
    @GetMapping("/")
    public ResponseEntity<?> getAllQuizzes() {
        try {
            List<Quiz> quizzes = quizService.getAll();
            return ResponseEntity.ok(quizzes);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // CREATE new quiz
    @PostMapping("/new/category/{catId}")
    public ResponseEntity<?> createQuiz(@PathVariable Long catId, @RequestBody Quiz quiz) {
        try{
            Quiz savedQuiz = quizService.createQuiz(catId, quiz);
            return ResponseEntity.ok(savedQuiz);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    // UPDATE existing quiz
    @PutMapping("/{id}/category/{catId}")
    public ResponseEntity<?> updateQuiz(@PathVariable Long id, @PathVariable Long catId,@RequestBody Quiz updatedQuiz) {
        try {
            Quiz quiz = quizService.updateQuiz(id, catId, updatedQuiz);
            return ResponseEntity.ok(quiz);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // DELETE quiz
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuiz(@PathVariable Long id) {
        try {
            quizService.deleteQuiz(id);
            return ResponseEntity.ok("Quiz supprimé avec succès (ID: " + id + ")");
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
