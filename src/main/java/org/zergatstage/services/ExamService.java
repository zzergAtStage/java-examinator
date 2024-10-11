package org.zergatstage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
  @Autowired
  private QuizAnswerService quizAnswerService;


  /**
   * Grades the answers submitted by the user and stores the results.
   *
   * @param exam Raw type of Exam class
   * @return result (int)
   */
  @Transactional
  public int gradeExam(Exam exam) {
    int result = 0;
    for (ExamSection section : exam.getSections()) {
      for (Questions questions : section.getQuestions()) {
        result = getTotalScore(questions.getQuestion(), questions, result);
        userAnswerRepository.save(questions);
      }
    }
    examRepository.save(exam);
    return result;
  }


  /**
   * Grades the answers submitted by the user and stores the results.
   *
   * @param submission ExamSubmissionDTO The submitted answers and session info
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
        Questions questions = new Questions();
        questions.setQuestion(question);
        questions.setUserAnswers(answerDTO.getAnswers());

        // Check if the answer is correct
        totalScore = getTotalScore(question, questions, totalScore);

        userAnswerRepository.save(questions);
      }
    }

    return totalScore;
  }

  private int getTotalScore(JavaQuizQuestion question, Questions questions, int totalScore) {
    if (quizAnswerService.isAnswerCorrect(question, questions)) {
      questions.setCorrect(true);
      questions.setPointsAwarded(question.getPoints()); // Assuming each question has points
      totalScore += question.getPoints();
    } else {
      questions.setCorrect(false);
      questions.setPointsAwarded(0);
    }
    return totalScore;
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
              .questions(getQuestionsPool(queue, numberQuestions))
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
    List<JavaQuizQuestion> list = questionRepository.findByQuestionHeader(javaQuizQuestion.getQuestionHeader());

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
            new IllegalArgumentException("No question with id " + id + " is found"));
  }

  public void updateQuestion(Long id, JavaQuizQuestion question) {
    question.setId(id);// father 9.10.2024:02:27  like a bone in throat
    questionRepository.save(question);
  }
}
