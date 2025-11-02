package com.l3mdw1.quiz_app.config;

import com.l3mdw1.quiz_app.model.*;
import com.l3mdw1.quiz_app.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;
    private final QuestionRepository questionRepo;
    private final AnswerRepository answerRepo;
    private final CategoryScoreboardRepository categoryScoreboardRepo;

    public DataInitializer(UserRepository userRepo,
                           CategoryRepository categoryRepo,
                           QuestionRepository questionRepo,
                           AnswerRepository answerRepo,
                           CategoryScoreboardRepository categoryScoreboardRepo) {
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
        this.questionRepo = questionRepo;
        this.answerRepo = answerRepo;
        this.categoryScoreboardRepo = categoryScoreboardRepo;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=== DATABASE INITIALIZER ===");


        if(answerRepo.count()<3)
            answerRepo.deleteAll();

        if(categoryRepo.count() <3)
            questionRepo.deleteAll();

        if(questionRepo.count() <3)
            categoryRepo.deleteAll();

        if(userRepo.count() <3)
            userRepo.deleteAll();

        if(userRepo.count()<1)
            if (ask("[?] Insert User data?")) insertUsers();
        if(categoryRepo.count()<1)
            if (ask("[?] Insert Category data?")) insertCategories();
        if(questionRepo.count()<1 && categoryScoreboardRepo.count()<1)
            if (ask("[?] Insert Question/Answer test data?")) {
                insertQAs();
                insertScores();
            }

        System.out.println("✅ Database initialization complete.\n");
    }

    @Transactional
    private List<User> insertUsers() {
        User admin = new User("admin", "admin@test.com", "admin123", true);
        User user1 = new User("mohamed", "mohamed@test.com", "password", false);
        List<User> users = userRepo.saveAll(List.of(admin, user1));

        System.out.println("/!\\ Users data inserted successfully.");
        return users;
    }

    @Transactional
    private List<Category> insertCategories() {
        Category math = new Category("Mathématiques");
        Category science = new Category("Sciences");
        Category history = new Category("Histoire");

        List<Category> categories = categoryRepo.saveAll(List.of(math, science, history));

        System.out.println("/!\\ Categories data inserted successfully.");
        return categories;
    }

    @Transactional
    private void insertQAs() {
        if (categoryRepo.count() < 3) {
            System.out.println("/?\\ You must first insert at least 3 categories before adding questions.");
            return;
        }
        List<Category> categories = categoryRepo.findAll();

        // Q1
        Question q1 = new Question("Combien font 5 + 3 ?", categories.get(0));
        List<Answer> q1Answers = List.of(
                new Answer("6", false, q1),
                new Answer("7", false, q1),
                new Answer("8", true, q1),
                new Answer("9", false, q1)
        );
        q1.setAnswers(q1Answers);
        questionRepo.save(q1);
        answerRepo.saveAll(q1Answers);

        // Q2
        Question q2 = new Question("Quel est l’état de l’eau à 100°C ?", categories.get(1));
        List<Answer> q2Answers = List.of(
                new Answer("Solide", false, q2),
                new Answer("Liquide", false, q2),
                new Answer("Gazeux", true, q2),
                new Answer("Plasma", false, q2)
        );
        q2.setAnswers(q2Answers);
        questionRepo.save(q2);
        answerRepo.saveAll(q2Answers);

        // Q3
        Question q3 = new Question("En quelle année a commencé la Seconde Guerre mondiale ?", categories.get(2));
        List<Answer> q3Answers = List.of(
                new Answer("1914", false, q3),
                new Answer("1939", true, q3),
                new Answer("1945", false, q3),
                new Answer("1950", false, q3)
        );
        q3.setAnswers(q3Answers);
        questionRepo.save(q3);
        answerRepo.saveAll(q3Answers);

        System.out.println("/!\\ Questions and answers inserted successfully!");
    }

    @Transactional
    private void insertScores() {
        if (ask("[?] Insert test records for the Score table?")) {
            categoryScoreboardRepo.deleteAll();
            System.out.println("/!\\ Scores table has been cleared.");
        }

        List<User> users = userRepo.findAll();
        List<Category> categories = categoryRepo.findAll();
        int minCount = Math.min(users.size(), categories.size());

        List<CategoryScoreboard> scores = new ArrayList<>();
        for (int i = 0; i < minCount; i++) {
            int randomScore = ThreadLocalRandom.current().nextInt(1, 101);
            scores.add(new CategoryScoreboard(categories.get(i), users.get(i), randomScore));
        }

        categoryScoreboardRepo.saveAll(scores);
        System.out.println("Inserted " + scores.size() + " test scores successfully.");
    }

    private boolean ask(String message) {
        Scanner sc = new Scanner(System.in);
        System.out.print(message + " [Y/n] >> ");
        String response = sc.nextLine().trim().toLowerCase();
        return response.isEmpty() || response.startsWith("y");
    }
}
