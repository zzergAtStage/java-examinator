package org.zergatstage.services;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.zergatstage.DTO.ExamSubmissionDTO;
import org.zergatstage.DTO.UserAnswerDTO;
import org.zergatstage.model.*;
import org.zergatstage.repository.*;
import org.zergatstage.services.answer.QuizAnswerService;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author father
 */
@Service
@RequiredArgsConstructor
public class ExamService {

    private static final int SECTIONS_AMOUNT = 3;

    private final JavaQuizRepository questionRepository;
    private final ExamRepository examRepository;
    private final ExamSectionRepository examSectionRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final QuizAnswerService quizAnswerService;


    /**
     * Grades the answers submitted by the user and stores the results.
     *
     * @param exam Raw type of Exam class
     * @return result (int)
     */
    @Transactional
    public SubmissionResult gradeAndSaveExam(Exam exam) {
        SubmissionResult submissionResult = new SubmissionResult();
        for (ExamSection section : exam.getSections()) {
            for (Questions submittedAnswer : section.getQuestions()) {
                submissionResult.increaseTotalAnswered();
                updateIntermediateScore(submittedAnswer, submissionResult); //code smells
                userAnswerRepository.save(submittedAnswer);
            }
        }
        examRepository.save(exam);
        submissionResult.setSubmissionId(exam.getSessionId());
        submissionResult.setParticipantName(exam.getUser().getUsername());
        return submissionResult;
    }


    /**
     * Grades the answers submitted by the user and stores the results.
     *
     * @param submission ExamSubmissionDTO The submitted answers and session info
     */
    public void gradeAndSaveExam(ExamSubmissionDTO submission) {
        // Retrieve the user and create a new exam entry
        User user = userRepository.findById(submission.getUserId()).orElseThrow();
        Exam exam = new Exam();
        SubmissionResult submissionResult = new SubmissionResult();
        exam.setSessionId(submission.getSessionId());
        exam.setUser(user);
        exam.setExamDate(LocalDateTime.now());
        examRepository.save(exam);

        // Iterate through each section
        for (Map.Entry<String, List<UserAnswerDTO>> sectionEntry : submission.getSectionAnswers().entrySet()) {
            ExamSection section = new ExamSection();
            section.setSectionName(sectionEntry.getKey());
            examSectionRepository.save(section);

            // Grade each question in the section
            for (UserAnswerDTO answerDTO : sectionEntry.getValue()) {
                JavaQuizQuestion question = questionRepository.findById(answerDTO.getQuestionId()).orElseThrow();
                Questions questions = new Questions();
                questions.setQuestion(question);
                questions.setUserAnswers(answerDTO.getAnswers());

                updateIntermediateScore(questions, submissionResult);
                userAnswerRepository.save(questions);
            }
        }
    }

    private void updateIntermediateScore(Questions submittedAnswer, SubmissionResult submissionResult) {
        JavaQuizQuestion question = submittedAnswer.getQuestion();
        if (quizAnswerService.isAnswerCorrect(question, submittedAnswer)) {
            submittedAnswer.setCorrect(true);
            submittedAnswer.setPointsAwarded(question.getPoints()); // Assuming each question has points
            submissionResult.increaseTotalScore(submittedAnswer.getPointsAwarded());
        } else {
            submittedAnswer.setCorrect(false);
            submittedAnswer.setPointsAwarded(0);
            submissionResult.increaseFalseAnswered();
        }
    }

    /**
     * Generates exam entity and related entities and question set
     *
     * @param difficulty      From 1 to 3 difficulty growing
     * @param numberQuestions Number of questions
     * @return Exam entity
     */
    public Exam getExam(int difficulty, int numberQuestions) {
        return getExam(userService.registerUser("DummyUser"), difficulty, numberQuestions);
    }

    /**
     * Generates exam entity and related entities and question set
     *
     * @param user            User object
     * @param difficulty      From 1 to 3 difficulty growing
     * @param numberQuestions Number of questions
     * @return Exam entity
     */
    public Exam getExam(User user, int difficulty, int numberQuestions) {
        List<JavaQuizQuestion> questions = questionRepository.findByDifficultyLevelLessThanEqual(difficulty);
        //PoC - view all questions
        // Ensure we have enough questions to create 3 sections with the specified number of questions
        if (questions.isEmpty()) {
            throw new IllegalArgumentException("Not enough questions available for the exam.");
        }
        Collections.shuffle(questions);
        Queue<JavaQuizQuestion> queue = new ArrayDeque<>(questions);
        List<ExamSection> sections = new ArrayList<>();
        for (int i = 0; i < SECTIONS_AMOUNT; i++) {
            if (queue.isEmpty()) break;
            sections.add(ExamSection.builder()
                    .sectionName("Section #" + (i + 1))
                    .questions(getQuestionsPool(queue, numberQuestions))
                    .build());

        }
        //List<ExamSection> sectionsSaved = examSectionRepository.saveAll(sections);

        //We're saving new exam, to grade it with submitted
        return examRepository.save(Exam.builder()
                .examDate(LocalDateTime.now())
                .user(user)
                .sections(sections)
                .sessionId(UUID.randomUUID().toString())
                .build());
    }

    private List<Questions> getQuestionsPool(Queue<JavaQuizQuestion> queue, int numberQuestions) {
        List<Questions> questions = new ArrayList<>();

        // Dequeue the specified number of questions
        for (int i = 0; i < numberQuestions && !queue.isEmpty(); i++) {
            JavaQuizQuestion question = queue.poll(); // poll() removes the head of the queue
            if (question != null) {
                questions.add(Questions.builder()
                        .question(question)
                        .build());
            }
        }

        return questions;
    }
    @Transactional(readOnly = true)
    public Exam getSubmittedExamBySessionId(String sessionId) {
        Exam bySessionId = examRepository.findBySessionId(sessionId).orElseThrow();
        if (bySessionId != null) {
            // Force initialization of lazy collections
            Hibernate.initialize(bySessionId.getSections());
            if (bySessionId.getSections() != null) {
                bySessionId.getSections().forEach(section ->
                        Hibernate.initialize(section.getQuestions())
                );
            }
        }
        return bySessionId;
    }

    public List<Exam> getSubmittedExamsByUser(User user) {
        return examRepository.findByUser(user);
    }

    /**
     * Creates a new Exam object that mirrors the structure of an already taken exam.
     *
     * @param originalSessionId The session ID of the previously taken exam
     * @param newUserId         The user ID for whom the new exam is being prepared
     * @return A new Exam instance with copied structure (sections/questions)
     */
    @Transactional
    public Exam prepareExamLikeTaken(String originalSessionId, Long newUserId) {
        // Fetch original exam
        Exam originalExam = examRepository.findBySessionId(originalSessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Original exam not found"));

        // Fetch user
        User user = userRepository.findById(newUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Create new exam
        Exam newExam = new Exam();
        newExam.setUser(user);
        newExam.setExamDate(LocalDateTime.now());
        newExam.setSessionId(UUID.randomUUID().toString()); // New session ID
        examRepository.save(newExam);
        List<ExamSection> newSections = new ArrayList<>();
        // Clone structure: sections and questions
        for (ExamSection originalSection : originalExam.getSections()) {
            ExamSection newSection = new ExamSection();
            newSection.setSectionName(originalSection.getSectionName());
            List<Questions> newSectionQuestions = new ArrayList<>();
            for (Questions originalQuestionRecord : originalSection.getQuestions()) {
                JavaQuizQuestion javaQuizQuestion = questionRepository.findById(originalQuestionRecord.getQuestion().getId()).orElseThrow();
                Questions newQuestionRecord = Questions.builder()
                        .question(javaQuizQuestion)
                        .build();
                newSectionQuestions.add(newQuestionRecord);
            }
            newSection.setQuestions(newSectionQuestions);
            newSections.add(newSection);
        }
        newExam.setSections(newSections);
        return newExam;
    }
    public void saveSubmittedExam(Exam exam) {
        examRepository.save(exam);
    }

    public void deleteSubmission(Exam exam) {
        examRepository.delete(exam);
    }

    /**
     * Saves a unique Java quiz question to the repository.
     * Throws an exception if a duplicate question exists.
     *
     * @param javaQuizQuestion The question to be saved.
     * @throws IllegalArgumentException If a question with the same header and correct answer already exists.
     */
    public void saveUniqueQuestion(JavaQuizQuestion javaQuizQuestion) {
        ensureQuestionIsUnique(javaQuizQuestion);
        // Save the new question if no duplicate exists
        questionRepository.save(javaQuizQuestion);
    }

    /**
     * Ensures the question is unique in the repository.
     *
     * @param javaQuizQuestion JavaQuizQuestion object
     * @throws IllegalArgumentException If a question with the same header and correct answer already exists.
     */
    public void ensureQuestionIsUnique(JavaQuizQuestion javaQuizQuestion) {
        // Find questions with the same header
        List<JavaQuizQuestion> list = questionRepository.findByQuestionHeader(javaQuizQuestion.getQuestionHeader().trim());

        // Check if any question in the list has the same correct answer (ignoring case) todo: implement uniqueness check
//    boolean duplicateExists = list.stream()
//            .anyMatch();

        // If a duplicate is found, throw an exception
//    if (duplicateExists) {
//      throw new IllegalArgumentException("Question with header: \"" + javaQuizQuestion.getQuestionHeader() +
//              "\" and answer: \"" + javaQuizQuestion.getCorrectAnswer()
//              + "\" already exists with the same correct answer.");
//    }
    }

    public JavaQuizQuestion getQuestionById(Long id) {
        return questionRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No question with id " + id + " is found"));
    }

    public void updateQuestion(Long id, JavaQuizQuestion question) {
        question.setId(id);// father 9.10.2024:02:27  like a bone in throat
        questionRepository.save(question);
    }
}
