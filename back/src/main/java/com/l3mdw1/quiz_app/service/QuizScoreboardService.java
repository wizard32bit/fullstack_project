package com.l3mdw1.quiz_app.service;

import com.l3mdw1.quiz_app.model.Quiz;
import com.l3mdw1.quiz_app.model.QuizScoreboard;
import com.l3mdw1.quiz_app.model.QuizScoreboardId;
import com.l3mdw1.quiz_app.model.User;
import com.l3mdw1.quiz_app.repository.QuizRepository;
import com.l3mdw1.quiz_app.repository.QuizScoreboardRepository;
import com.l3mdw1.quiz_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuizScoreboardService {

    @Autowired
    private QuizScoreboardRepository scoreboardRepo;

    @Autowired
    private QuizRepository quizRepo;

    @Autowired
    private UserRepository userRepo;


    @Transactional
    public QuizScoreboard assignScore(Long quizId, Long userId, int passedQs, int failedQs, int score) {
        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        QuizScoreboard scoreboard = scoreboardRepo.findById(new QuizScoreboardId(quizId, userId))
                .orElse(new QuizScoreboard(quiz, user, passedQs, failedQs, score));

        scoreboard.setPassedQs(passedQs);
        scoreboard.setFailedQs(failedQs);
        scoreboard.setScore(score);

        return scoreboardRepo.save(scoreboard);
    }

    public List<QuizScoreboard> getScoresByQuiz(Long quizId) {
        return scoreboardRepo.findByQuizId(quizId);
    }

    public List<QuizScoreboard> getScoresByUser(Long userId) {
        return scoreboardRepo.findByUserId(userId);
    }

    @Transactional
    public void deleteScore(Long quizId, Long userId) {
        QuizScoreboardId id = new QuizScoreboardId(quizId, userId);
        if (scoreboardRepo.existsById(id)) {
            scoreboardRepo.deleteById(id);
        } else {
            throw new RuntimeException("Score not found for quizId: " + quizId + " and userId: " + userId);
        }
    }
}
