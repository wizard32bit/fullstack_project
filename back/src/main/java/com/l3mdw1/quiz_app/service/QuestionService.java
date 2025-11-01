package com.l3mdw1.quiz_app.service;

import com.l3mdw1.quiz_app.controller.AnswerController;
import com.l3mdw1.quiz_app.model.Answer;
import com.l3mdw1.quiz_app.model.Question;
import com.l3mdw1.quiz_app.model.Quiz;
import com.l3mdw1.quiz_app.repository.QuestionRepository;
import com.l3mdw1.quiz_app.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private QuizRepository quizRepo;

    public List<Question> getAll() {
        List<Question> questions = questionRepo.findAll();
        if (questions.isEmpty()) {
            throw new RuntimeException("Aucune question enregistrée dans la base de données");
        }
        return questions;
    }

    public Question getQuestion(Long id) {
        return questionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Aucune quiz avec cet id: " + id));
    }

    public List<Question> getQuestionsByQuizId(Long id){
        Quiz quiz= quizRepo.findById(id).orElseThrow(()-> new RuntimeException("Aucune question avec cet id: "+ id));
        List<Question> questions= quiz.getQuestions();

        if(questions==null || questions.isEmpty()){
            throw new RuntimeException("Aucun question enregistré pour cette quiz");
        }
        return questions;
    }

    @Transactional
    public Question createQuestion(Question question) {
        System.out.println("=========================\n");
        System.out.println(question.getQuiz());
        System.out.println("=========================\n");

        if (question.getQuiz() == null) {
            throw new RuntimeException("Le quiz associé à la question est manquant");
        }

        if (question.getQuiz().getId() == null) {
            throw new RuntimeException("Le quiz associé à la question a un id invalide");
        }

        // fetch the quiz from repo
        Quiz quiz = quizRepo.findById(question.getQuiz().getId())
                .orElseThrow(() -> new RuntimeException("Aucun quiz trouvé avec l'id: " + question.getQuiz().getId()));

        if (question.getAnswers() != null) {
            for (Answer answer : question.getAnswers()) {
                if (answer.getContent() == null || answer.getContent().trim().isEmpty()) {
                    throw new RuntimeException("Chaque réponse doit avoir un contenu");
                }
            }
        }

        return questionRepo.save(question);
    }

    @Transactional
    public Question createQuestionByQuizId(Long quizId, Question question) {
        // Fetch quiz
        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Aucun quiz trouvé avec l'id: " + quizId));

        boolean duplicateQuestionExists= quiz.getQuestions().stream().anyMatch(question1 -> question.getContent().equals(question1.getContent()));
        if(duplicateQuestionExists){
            throw new RuntimeException("Cette question déja existe dans cet quiz");
        }
        // Set quiz on question
        question.setQuiz(quiz);

        // Validate answers
        if (question.getAnswers() != null) {
            for (Answer answer : question.getAnswers()) {
                if (answer.getContent() == null || answer.getContent().trim().isEmpty()) {
                    throw new RuntimeException("Chaque réponse doit avoir un contenu");
                }
            }

            // Check correctness rules
            boolean hasAtLeastOneCorrect = question.getAnswers().stream().anyMatch(Answer::isCorrect);
            boolean hasMorethanOneCorrectCount = question.getAnswers().stream()
                    .filter(Answer::isCorrect)
                    .count() > 1;
            boolean allFalse = question.getAnswers().stream().noneMatch(Answer::isCorrect);

            if (!hasAtLeastOneCorrect) {
                throw new RuntimeException("Au moins une réponse doit être correcte");
            }

            if (hasMorethanOneCorrectCount) {
                throw new RuntimeException("Il faut avoir seulement une seule réponse correcte par question");
            }

            if (question.getAnswers().size() == 4 && allFalse){
                    throw new RuntimeException("Toutes les réponses ne peuvent pas être incorrectes");
            }

        }
        return questionRepo.save(question);
    }

    @Transactional
    public Question updateQuestion(Long id, Question updatedQuestion) {
        return updateQuestionInternal(id, updatedQuestion, null);
    }

    @Transactional
    public Question updateQuestionQuiz(Long id, Question updatedQuestion, Long quizId) {
        return updateQuestionInternal(id, updatedQuestion, quizId);
    }

    @Transactional
    public Question updateQuestionInternal(Long id, Question updatedQuestion, Long quizId) {
        Question questionFromDB = getQuestion(id);

        // Update content
        if (updatedQuestion.getContent() != null && !updatedQuestion.getContent().trim().isEmpty()) {
            questionFromDB.setContent(updatedQuestion.getContent().trim());
        }

        // Validate answers
        if (updatedQuestion.getAnswers() == null || updatedQuestion.getAnswers().isEmpty()) {
            throw new RuntimeException("!! Validation de la requete: structure des réponses invalide");
        }
        List<Answer> answers = updatedQuestion.getAnswers();
        long correctCount = answers.stream().filter(Answer::isCorrect).count();
        if (correctCount != 1) {
            throw new RuntimeException("Une question doit avoir exactement une seule réponse correcte");
        }
        if (answers.size() > 4) {
            throw new RuntimeException("Une question doit avoir au plus 4 réponses");
        }

        // Update existing answers and add new ones
        for (Answer updatedAnswer : answers) {
            if (updatedAnswer.getId() != null) {
                // Existing answer → update
                Answer existing = questionFromDB.getAnswers().stream()
                        .filter(a -> a.getId().equals(updatedAnswer.getId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Réponse introuvable: " + updatedAnswer.getId()));
                existing.setContent(updatedAnswer.getContent().trim());
                existing.setCorrect(updatedAnswer.isCorrect());
            } else {
                // New answer → add
                if(questionFromDB.getAnswers().size()<4){
                    updatedAnswer.setQuestion(questionFromDB);
                    questionFromDB.getAnswers().add(updatedAnswer);
                }else
                    throw new RuntimeException("Cette question contient 4 réponses, l'ajout des autres réposnes est impossible.");
            }
        }

        // Optionally change quiz
        if (quizId != null) {
            Quiz newQuiz = quizRepo.findById(quizId)
                    .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
            questionFromDB.setQuiz(newQuiz);
        }

        return questionRepo.save(questionFromDB);
    }


    public void deleteQuestion(Long id) {
        Question question = getQuestion(id);
        questionRepo.delete(question);
    }

}
