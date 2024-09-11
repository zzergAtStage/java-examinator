package org.zergatstage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zergatstage.DTO.ExamSubmissionDTO;
import org.zergatstage.DTO.UserAnswerDTO;
import org.zergatstage.model.*;
import org.zergatstage.repository.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author father
 */
@Service
public class ExamService {

    private static final int SECTIONS_NUMBER = 3;
    @Autowired
    private JavaQuizRepository questionRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamSectionRepository examSectionRepository;
    @Autowired
    private UserAnswerRepository userAnswerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    /**
     * Grades the answers submitted by the user and stores the results.
     *
     * @param submission The submitted answers and session info
     * @return The total score for the exam.
     */
    public int gradeExam(ExamSubmissionDTO submission) {
        // Retrieve the user and create a new exam entry
        User user = userRepository.findById(submission.getUserId()).orElseThrow();
        Exam exam = new Exam();
        exam.setSessionId(submission.getSessionId());
        exam.setUser(user);
        exam.setExamDate(LocalDateTime.now());
        examRepository.save(exam);

        int totalScore = 0;

        // Iterate through each section
        for (Map.Entry<String, List<UserAnswerDTO>> sectionEntry : submission.getSectionAnswers().entrySet()) {
            ExamSection section = new ExamSection();
            section.setSectionName(sectionEntry.getKey());
            examSectionRepository.save(section);

            // Grade each question in the section
            for (UserAnswerDTO answerDTO : sectionEntry.getValue()) {
                JavaQuizQuestion question = questionRepository.findById(answerDTO.getQuestionId()).orElseThrow();
                UserAnswer userAnswer = new UserAnswer();
                userAnswer.setQuestion(question);
                userAnswer.setUserAnswer(answerDTO.getAnswer());

                // Check if the answer is correct
                if (question.getCorrectAnswer().equalsIgnoreCase(answerDTO.getAnswer())) {
                    userAnswer.setCorrect(true);
                    userAnswer.setPointsAwarded(question.getPoints()); // Assuming each question has points
                    totalScore += question.getPoints();
                } else {
                    userAnswer.setCorrect(false);
                    userAnswer.setPointsAwarded(0);
                }

                userAnswerRepository.save(userAnswer);
            }
        }

        return totalScore;
    }

    /**
     * Generates exam entity and related entities and question set
     *  @param difficulty      From 1 to 3 difficulty growing
     *  @param numberQuestions Number of questions
     *  @return Exam entity
     */
    public Exam getExam(int difficulty, int numberQuestions) {
        return getExam(userService.registerUser("DummyUser"), difficulty,numberQuestions);
    }

    /**
     * Generates exam entity and related entities and question set
     * @param user            User object
     * @param difficulty      From 1 to 3 difficulty growing
     * @param numberQuestions Number of questions
     * @return Exam entity
     */
    public Exam getExam(User user, int difficulty, int numberQuestions) {
        List<JavaQuizQuestion> questions = questionRepository.findByDifficultyLevelLessThanEqual(difficulty);
        // Ensure we have enough questions to create 3 sections with the specified number of questions
        if (questions.size() < 3 * numberQuestions) {
            throw new IllegalArgumentException("Not enough questions available for the exam.");
        }
        Collections.shuffle(questions);
        Queue<JavaQuizQuestion> queue = new ArrayDeque<>(questions);
        List<ExamSection> sections = new ArrayList<>();
        for (int i = 0; i < SECTIONS_NUMBER; i++) {
            sections.add(ExamSection.builder()
                    .sectionName("Section #" + (i + 1))
                    .userAnswers(getQuestionsPool(queue, numberQuestions))
                    .build());

        }
        List<ExamSection> sectionsSaved = examSectionRepository.saveAll(sections);

        //We're saving new exam, to grade it with submitted
        return examRepository.save(Exam.builder()
                .examDate(LocalDateTime.now())
                .user(user)
                .sections(sectionsSaved)
                .sessionId(UUID.randomUUID().toString())
                .build());
    }

    private List<UserAnswer> getQuestionsPool(Queue<JavaQuizQuestion> queue, int numberQuestions) {
        List<UserAnswer> userAnswers = new ArrayList<>();

        // Dequeue the specified number of questions
        for (int i = 0; i < numberQuestions && !queue.isEmpty(); i++) {
            JavaQuizQuestion question = queue.poll(); // poll() removes the head of the queue
            if (question != null) {
                userAnswers.add(UserAnswer.builder()
                        .question(question)
                        .build());
            }
        }

        return userAnswers;
    }

    public Exam getSubmittedExamBySessionId(String sessionId) {
        return examRepository.findBySessionId(sessionId);
    }

    public List<Exam> getSubmittedExamsByUser(User user) {
        return examRepository.findByUser(user);
    }
    public void saveSubmittedExam(Exam exam) {
        examRepository.save(exam);
    }

    public void deleteSubmission(Exam exam) {
        examRepository.delete(exam);
    }
}
