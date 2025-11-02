package com.l3mdw1.quiz_app.service;

import com.l3mdw1.quiz_app.model.Question;
import com.l3mdw1.quiz_app.model.QuestionQuiz;
import com.l3mdw1.quiz_app.model.QuestionQuizId;
import com.l3mdw1.quiz_app.model.Quiz;
import com.l3mdw1.quiz_app.repository.QuestionQuizRepository;
import com.l3mdw1.quiz_app.repository.QuestionRepository;
import com.l3mdw1.quiz_app.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionQuizService {
    @Autowired private QuizRepository quizRepo;
    @Autowired private QuestionRepository questionRepo;
    @Autowired private QuestionQuizRepository questionQuizRepo;

    public QuestionQuiz assignQuestionToQuiz(Long quizId, Long questionId, QuestionQuiz reqBody) {
        if (reqBody == null) {
            throw new RuntimeException("Invalid request body");
        }

        QuestionQuizId questionQuizId = new QuestionQuizId(quizId, questionId);
        Optional<QuestionQuiz> questionQuizDB = questionQuizRepo.findById(questionQuizId);

        if (questionQuizDB.isPresent()) {
            throw new RuntimeException("Cette question a déjà été répondue dans ce quiz. Aucune duplication n'est acceptée.");
        }

        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Aucun quiz avec cet id: "+ quizId));
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Aucun question avec cet id: "+ questionId));

        quiz.getQuestions().add(question);
        QuestionQuiz questionQuiz = new QuestionQuiz(quiz, question, reqBody.isAnsweredCorrectly());
        return questionQuizRepo.save(questionQuiz);
    }

    public List<QuestionQuiz> getQuestionQuizByQuizId(Long quizId){
        List<QuestionQuiz> questionsQuiz= questionQuizRepo.findByQuizId(quizId);
        if(questionsQuiz.isEmpty()){
            throw new RuntimeException("Aucun quiz a été passé avec cet ID");
        }
        return questionsQuiz;
    }
}
