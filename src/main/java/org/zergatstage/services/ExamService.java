package org.zergatstage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.zergatstage.model.dto.ExamSubmissionDTO;
import org.zergatstage.model.dto.UserAnswerDTO;
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
  private QuestionRepository questionRepository;

  @Autowired
  private QuizRepository quizRepository;

  @Autowired
  private SectionRepository sectionRepository;
  @Autowired
  private QuizAttemptRepository quizAttemptRepository;
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
    for (Section section : exam.getSections()) {
      for (Question questions : section.getQuestions()) {
        result += 1; //getTotalScore(questions.getQuestion(), questions);
        try {
          //userAnswerRepository.save(questions);
        } catch (Exception e) {
          e.printStackTrace();
          throw e;
        }
      }
    }
    quizRepository.save(exam);
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
    QuizAttempt quizAttempt = QuizAttempt.start(user,submission);

    int totalScore = 0;

    // Iterate through each section
    for (Map.Entry<String, List<UserAnswerDTO>> sectionEntry : submission.getSectionAnswers().entrySet()) {
      Section section = new Section();
      section.setSectionName(sectionEntry.getKey());
      sectionRepository.save(section);

      // Grade each question in the section
      for (UserAnswerDTO answerDTO : sectionEntry.getValue()) {
        Question question = questionRepository.findById(answerDTO.getQuestionId()).orElseThrow();
        Questions questions = new Questions();
        questions.setQuestion(question);
        questions.setUserAnswers(answerDTO.getAnswers());

        // Check if the answer is correct
        totalScore = getTotalScore(question, questions, totalScore);

        quizAttemptRepository.save(questions);
      }
    }

    return totalScore;
  }

  private int getTotalScore(Question question, Questions questions, int totalScore) {
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
    List<Question> questions = questionRepository.findByDifficultyLevelLessThanEqual(difficulty);
    //PoC - view all questions
    // Ensure we have enough questions to create 3 sections with the specified number of questions
    if (questions.isEmpty()) {
      throw new IllegalArgumentException("Not enough questions available for the exam.");
    }
    Collections.shuffle(questions);
    Queue<Question> queue = new ArrayDeque<>(questions);
    List<Section> sections = new ArrayList<>();
    for (int i = 0; i < SECTIONS_NUMBER; i++) {
      if (queue.isEmpty()) break;
//      sections.add(ExamSection.builder()
//              .sectionName("Section #" + (i + 1))
//              .questions(getQuestionsPool(queue, numberQuestions))
//              .build());

    }
    List<Section> sectionsSaved = sectionRepository.saveAll(sections);

    //We're saving new exam, to grade it with submitted
    return quizRepository.save(Exam.builder()
            .examDate(LocalDateTime.now())
            .user(user)
            .sections(sectionsSaved)
            .sessionId(UUID.randomUUID().toString())
            .build());
  }

  private List<Questions> getQuestionsPool(Queue<Question> queue, int numberQuestions) {
    List<Questions> questions = new ArrayList<>();

    // Dequeue the specified number of questions
    for (int i = 0; i < numberQuestions && !queue.isEmpty(); i++) {
      Question question = queue.poll(); // poll() removes the head of the queue
      if (question != null) {
        questions.add(Questions.builder()
                .question(question)
                .build());
      }
    }

    return questions;
  }

  public Exam getSubmittedExamBySessionId(String sessionId) {
    return quizRepository.findBySessionId(sessionId);
  }

  public List<Exam> getSubmittedExamsByUser(User user) {
    return quizRepository.findByUser(user);
  }

  public void saveSubmittedExam(Exam exam) {
    quizRepository.save(exam);
  }

  public void deleteSubmission(Exam exam) {
    quizRepository.delete(exam);
  }

  /**
   * Saves a unique Java quiz question to the repository.
   * Throws an exception if a duplicate question exists.
   *
   * @param question The question to be saved.
   * @throws IllegalArgumentException If a question with the same header and correct answer already exists.
   */
  public void saveUniqueQuestion(Question question) {
    ensureQuestionIsUnique(question);
    // Save the new question if no duplicate exists
    questionRepository.save(question);
  }

  /**
   * Ensures the question is unique in the repository.
   *
   * @param question JavaQuizQuestion object
   * @throws IllegalArgumentException If a question with the same header and correct answer already exists.
   */
  public void ensureQuestionIsUnique(Question question) {
    // Find questions with the same header
    List<Question> list = questionRepository.findByQuestionHeader(question.getQuestionHeader());

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

  public Question getQuestionById(Long id) {
    return questionRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "No question with id " + id + " is found"));
  }

  public void updateQuestion(Long id, Question question) {
    question.setId(id);// father 9.10.2024:02:27  like a bone in throat
    questionRepository.save(question);
  }
}
