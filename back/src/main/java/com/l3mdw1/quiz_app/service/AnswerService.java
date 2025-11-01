package com.l3mdw1.quiz_app.service;

import com.l3mdw1.quiz_app.model.Answer;
import com.l3mdw1.quiz_app.model.Question;
import com.l3mdw1.quiz_app.repository.AnswerRepository;
import com.l3mdw1.quiz_app.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepo;

    @Autowired
    private QuestionRepository questionRepo;

    // Create a new answer for a given question ID
    public Answer createAnswer(Long questionId, Answer answer) {
        if (answer == null || answer.getContent() == null || answer.getContent().trim().isEmpty()) {
            throw new RuntimeException("Le contenu de la réponse est invalide");
        }
        Question question = questionRepo.findById(questionId).orElseThrow(() -> new RuntimeException("Question introuvable pour l'ID : " + questionId));
        if (question.getAnswers().size() >= 4) {
            throw new RuntimeException("La question contient déjà 4 réponses");
        }


        // Debug: print current answers before adding
        System.out.println("Question ID: " + question.getId());
        System.out.println("Nombre de réponses existantes: " + question.getAnswers().size());
        for (Answer a : question.getAnswers()) {
            System.out.println("- Réponse: " + a.getContent() + " (correcte: " + a.isCorrect() + ")");
        }

        for (Answer a : question.getAnswers()) {
            if (a.getContent().trim().equals(answer.getContent().trim())) {
                throw new RuntimeException("Une question ne peut pas avoir une réponse dupliqué");
            }
        }

        if (answer.isCorrect()) {
            boolean alreadyCorrect = question.getAnswers().stream().anyMatch(Answer::isCorrect);
            if (alreadyCorrect) {
                throw new RuntimeException("Cette question a déjà une réponse correcte");
            }
        }

        if(question.getAnswers().size() == 3 && !answer.isCorrect()){
            boolean allAnswersFalse = question.getAnswers().stream().noneMatch(Answer::isCorrect);
            if(allAnswersFalse){
                throw new RuntimeException(
                        "Chaque question doit avoir au moins une réponse correcte. " +
                        "Cette réponse est fausse et son ajout rendrait toutes les réponses incorrectes."
                );
            }
        }

        question.getAnswers().add(answer);
        answerRepo.save(answer);
        questionRepo.save(question); // cascade = ALL handles saving the answer
        return answer;
    }

    public Answer getAnswer(Long id){
        return answerRepo.findById(id).orElseThrow(()-> new RuntimeException("Aucun réponse avec l'id: "+ id));
    }
    public List<Answer> getAll() {
        List<Answer> answers = answerRepo.findAll();
        if (answers.isEmpty()) {
            throw new RuntimeException("Aucune réponse enregistrée");
        }
        return answers;
    }

    public List<Answer> getAllByQuestionId(Long questionId) {
        Question q = questionRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Aucune question avec l'id: " + questionId));
        return q.getAnswers();
    }

    public Answer updateAnswer(Long questionId, Long answerId, Answer updatedAnswer) {
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question introuvable"));

        Answer existingAnswer = question.getAnswers().stream()
                .filter(a -> a.getId().equals(answerId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Réponse introuvable pour cet ID"));

        // BLOCK any update if question has 4 answers AND this one is the only correct answer
        boolean isOnlyCorrect = existingAnswer.isCorrect() &&
                question.getAnswers().stream()
                        .filter(a -> !a.getId().equals(answerId))
                        .noneMatch(Answer::isCorrect);

        if (question.getAnswers().size() == 4 && isOnlyCorrect) {
            throw new RuntimeException("Impossible de modifier cette réponse : c'est la seule correcte et la question contient déjà 4 réponses.");
        }

        // Update correctness (and ensure only one correct per question)
        if (updatedAnswer.isCorrect() && !existingAnswer.isCorrect()) {
            boolean otherAlreadyCorrect = question.getAnswers().stream()
                    .anyMatch(a -> a.isCorrect() && !a.getId().equals(answerId));
            if (otherAlreadyCorrect) {
                throw new RuntimeException("Cette question a déjà une réponse correcte");
            }
            existingAnswer.setCorrect(true);
        }

        // Update content
        if (updatedAnswer.getContent() != null && !updatedAnswer.getContent().trim().isEmpty()) {
            existingAnswer.setContent(updatedAnswer.getContent().trim());
        }

        questionRepo.save(question);
        return existingAnswer;
    }

    public void deleteAnswer(Long questionId, Long answerId) {
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question introuvable"));

        boolean removed = question.getAnswers().removeIf(a -> a.getId().equals(answerId));
        if (!removed) {
            throw new RuntimeException("Réponse introuvable dans cette question");
        }
        answerRepo.deleteById(answerId);
        questionRepo.save(question); // orphanRemoval = true deletes it automatically
    }
}
