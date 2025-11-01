package com.l3mdw1.quiz_app.config;

import com.l3mdw1.quiz_app.model.*;
import com.l3mdw1.quiz_app.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepo;
    private final QuizRepository quizRepo;
    private final QuestionRepository questionRepo;

    public DataInitializer(CategoryRepository categoryRepo,
                           QuizRepository quizRepo,
                           QuestionRepository questionRepo) {
        this.categoryRepo = categoryRepo;
        this.quizRepo = quizRepo;
        this.questionRepo = questionRepo;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (categoryRepo.count() > 0) {
            System.out.println("Initial data already exists. Skipping initialization.");
            return;
        }

        // === Create Categories ===
        Category science = new Category();
        science.setName("Science");

        Category history = new Category();
        history.setName("History");

        Category tech = new Category();
        tech.setName("Technology");

        categoryRepo.saveAll(Arrays.asList(science, history, tech));

        // === Create Quizzes ===
        Quiz quiz1 = new Quiz();
        quiz1.setTitle("Physics Basics");
        quiz1.setCategory(science);

        Quiz quiz2 = new Quiz();
        quiz2.setTitle("World War II");
        quiz2.setCategory(history);

        Quiz quiz3 = new Quiz();
        quiz3.setTitle("Programming Fundamentals");
        quiz3.setCategory(tech);

        quizRepo.saveAll(Arrays.asList(quiz1, quiz2, quiz3));

        // === Create Questions and Answers for Quiz 1 ===
        Question q1 = new Question();
        q1.setContent("What is the speed of light?");
        q1.setAnswers(Arrays.asList(
                createAnswer("300,000 km/s", true),
                createAnswer("150,000 km/s", false),
                createAnswer("1,000 km/s", false)
        ));
        q1.setQuiz(quiz1);

        Question q2 = new Question();
        q2.setContent("Who developed the theory of relativity?");
        q2.setAnswers(Arrays.asList(
                createAnswer("Isaac Newton", false),
                createAnswer("Albert Einstein", true),
                createAnswer("Nikola Tesla", false)
        ));
        q2.setQuiz(quiz1);

        // === Create Questions and Answers for Quiz 2 ===
        Question q3 = new Question();
        q3.setContent("In which year did World War II end?");
        q3.setAnswers(Arrays.asList(
                createAnswer("1945", true),
                createAnswer("1918", false),
                createAnswer("1939", false)
        ));
        q3.setQuiz(quiz2);

        Question q4 = new Question();
        q4.setContent("Who was the British Prime Minister during most of WWII?");
        q4.setAnswers(Arrays.asList(
                createAnswer("Winston Churchill", true),
                createAnswer("Neville Chamberlain", false),
                createAnswer("Margaret Thatcher", false)
        ));
        q4.setQuiz(quiz2);

        // === Create Questions and Answers for Quiz 3 ===
        Question q5 = new Question();
        q5.setContent("Which language is primarily used for Android development?");
        q5.setAnswers(Arrays.asList(
                createAnswer("Java", true),
                createAnswer("Python", false),
                createAnswer("C#", false)
        ));
        q5.setQuiz(quiz3);

        Question q6 = new Question();
        q6.setContent("What does HTML stand for?");
        q6.setAnswers(Arrays.asList(
                createAnswer("HyperText Markup Language", true),
                createAnswer("HyperText Markdown Language", false),
                createAnswer("HighText Machine Language", false)
        ));
        q6.setQuiz(quiz3);

        // Save all Questions
        questionRepo.saveAll(List.of(q1, q2, q3, q4, q5, q6));

        System.out.println("=== Test data inserted successfully ===");
    }

    private Answer createAnswer(String content, boolean isCorrect) {
        Answer a = new Answer();
        a.setContent(content);
        a.setCorrect(isCorrect);
        return a;
    }
}
