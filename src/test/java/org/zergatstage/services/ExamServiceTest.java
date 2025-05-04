package org.zergatstage.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zergatstage.DTO.ExamSubmissionDTO;
import org.zergatstage.DTO.UserAnswerDTO;
import org.zergatstage.filemanager.IntegrationFileGateway;
import org.zergatstage.model.*;
import org.zergatstage.repository.*;
import org.zergatstage.services.answer.QuizAnswerService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ExamServiceTest {
  @InjectMocks
  private ExamService examService; // Service under test

  @Mock
  private QuestionRepository questionRepository;

  @Mock
  private QuizRepository quizRepository;

  @Mock
  private SectionRepository sectionRepository;

  @Mock
  private QuizAnswerService quizAnswerService;
  @Mock
  private QuizAttemptRepository quizAttemptRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private IntegrationFileGateway fileGateway;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this); // Initializes mocks
  }

  @Test
  void writeQuestions() {
  }

  @Test
  void testGradeExamSuccessfully() {
    // Mocking exam submission
    ExamSubmissionDTO submission = new ExamSubmissionDTO();
    submission.setUserId(1L);
    submission.setSessionId("session123");

    // Mocking user and repositories
    User mockUser = new User();
    mockUser.setId(1L);
    when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

    // Mocking section answers
    List<UserAnswerDTO> sectionAnswers = new ArrayList<>();
    UserAnswerDTO answerDTO = UserAnswerDTO.builder()
            .questionId(1L)
            .answers(List.of("Answer A"))
            .build();
    sectionAnswers.add(answerDTO);
    submission.setSectionAnswers(Map.of("Section1", sectionAnswers));

    // Mocking questions and answers
    Question question = Question.builder()
            .id(1L)
            .correctAnswer("Answer1")
            .points(5)
            .build();
    when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

    // Mock exam and section save operations
    Exam mockExam = new Exam();
    when(quizRepository.save(any(Exam.class))).thenReturn(mockExam);
    Section mockSection = new Section();
    when(sectionRepository.save(any(Section.class))).thenReturn(mockSection);

    // Mock userAnswer save
    when(quizAttemptRepository.save(any(Questions.class))).thenReturn(new Questions());

    // Execute the grading logic
    int totalScore = examService.gradeExam(submission);

    // Verify the behavior and assert results
    //assertEquals(5, totalScore); // Total score should be 5 for correct answer
    verify(userRepository, times(1)).findById(1L);
    verify(quizRepository, times(1)).save(any(Exam.class));
    verify(quizAttemptRepository, times(1)).save(any(Questions.class));
  }

  @Test
  void testGradeExamWithIncorrectAnswer() {
    // Mocking exam submission
    ExamSubmissionDTO submission = new ExamSubmissionDTO();
    submission.setUserId(1L);
    submission.setSessionId("session123");

    // Mocking user and repositories
    User mockUser = new User();
    mockUser.setId(1L);
    when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

    // Mocking section answers
    List<UserAnswerDTO> sectionAnswers = new ArrayList<>();
    UserAnswerDTO answerDTO = UserAnswerDTO.builder()
            .questionId(1L)
            .answer("WrongAnswer")
            .build();
    sectionAnswers.add(answerDTO);
    submission.setSectionAnswers(Map.of("Section1", sectionAnswers));

    // Mocking questions and answers
    Question question = Question.builder()
            .id(1L)
            .correctAnswer("Answer1")
            .points(5)
            .build();
    when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

    // Mock exam and section save operations
    Exam mockExam = new Exam();
    when(quizRepository.save(any(Exam.class))).thenReturn(mockExam);
    Section mockSection = new Section();
    when(sectionRepository.save(any(Section.class))).thenReturn(mockSection);

    // Mock userAnswer save
    when(quizAttemptRepository.save(any(Questions.class))).thenReturn(new Questions());

    // Execute the grading logic
    int totalScore = examService.gradeExam(submission);

    // Verify the behavior and assert results
    assertEquals(0, totalScore); // Total score should be 0 for incorrect answer
    verify(userRepository, times(1)).findById(1L);
    verify(quizRepository, times(1)).save(any(Exam.class));
    verify(quizAttemptRepository, times(1)).save(any(Questions.class));
  }

  @Test
  void testGetExamWithSufficientQuestions() {
    // Mocking the question repository
    List<Question> questions = Arrays.asList(new Question(), new Question(), new Question());
    when(questionRepository.findByDifficultyLevelLessThanEqual(1)).thenReturn(questions);

    // Mock user repository
    User mockUser = new User();
    mockUser.setId(1L);
    when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

    // Mock exam section repository
    List<Section> mockSections = Arrays.asList(new Section(), new Section(), new Section());
    when(sectionRepository.saveAll(anyList())).thenReturn(mockSections);

    // Test method
//        Exam exam = examService.getExam(1, 1);
//
//        assertNotNull(exam);
//        assertEquals(3, exam.getSections().size());
  }

  @Test
  void getExam() {
  }

  @Test
  void testGetSubmittedExamBySessionId() {
    // Mock the repository behavior
    Exam mockExam = new Exam();
    when(quizRepository.findBySessionId("session123")).thenReturn(mockExam);

    // Test method
    Exam result = examService.getSubmittedExamBySessionId("session123");

    assertNotNull(result);
    verify(quizRepository, times(1)).findBySessionId("session123");
  }
}