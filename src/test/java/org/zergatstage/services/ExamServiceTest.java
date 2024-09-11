package org.zergatstage.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zergatstage.DTO.ExamSubmissionDTO;
import org.zergatstage.DTO.UserAnswerDTO;
import org.zergatstage.model.*;
import org.zergatstage.repository.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExamServiceTest {
    @InjectMocks
    private ExamService examService; // Service under test

    @Mock
    private JavaQuizRepository questionRepository;

    @Mock
    private ExamRepository examRepository;

    @Mock
    private ExamSectionRepository examSectionRepository;

    @Mock
    private UserAnswerRepository userAnswerRepository;

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
        UserAnswerDTO answerDTO = new UserAnswerDTO();
        answerDTO.setQuestionId(1L);
        answerDTO.setAnswer("Answer1");
        sectionAnswers.add(answerDTO);
        submission.setSectionAnswers(Map.of("Section1", sectionAnswers));

        // Mocking questions and answers
        JavaQuizQuestion question = new JavaQuizQuestion();
        question.setId(1L);
        question.setCorrectAnswer("Answer1");
        question.setPoints(5);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        // Mock exam and section save operations
        Exam mockExam = new Exam();
        when(examRepository.save(any(Exam.class))).thenReturn(mockExam);
        ExamSection mockSection = new ExamSection();
        when(examSectionRepository.save(any(ExamSection.class))).thenReturn(mockSection);

        // Mock userAnswer save
        when(userAnswerRepository.save(any(UserAnswer.class))).thenReturn(new UserAnswer());

        // Execute the grading logic
        int totalScore = examService.gradeExam(submission);

        // Verify the behavior and assert results
        assertEquals(5, totalScore); // Total score should be 5 for correct answer
        verify(userRepository, times(1)).findById(1L);
        verify(examRepository, times(1)).save(any(Exam.class));
        verify(userAnswerRepository, times(1)).save(any(UserAnswer.class));
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
        UserAnswerDTO answerDTO = new UserAnswerDTO();
        answerDTO.setQuestionId(1L);
        answerDTO.setAnswer("WrongAnswer");
        sectionAnswers.add(answerDTO);
        submission.setSectionAnswers(Map.of("Section1", sectionAnswers));

        // Mocking questions and answers
        JavaQuizQuestion question = new JavaQuizQuestion();
        question.setId(1L);
        question.setCorrectAnswer("Answer1");
        question.setPoints(5);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        // Mock exam and section save operations
        Exam mockExam = new Exam();
        when(examRepository.save(any(Exam.class))).thenReturn(mockExam);
        ExamSection mockSection = new ExamSection();
        when(examSectionRepository.save(any(ExamSection.class))).thenReturn(mockSection);

        // Mock userAnswer save
        when(userAnswerRepository.save(any(UserAnswer.class))).thenReturn(new UserAnswer());

        // Execute the grading logic
        int totalScore = examService.gradeExam(submission);

        // Verify the behavior and assert results
        assertEquals(0, totalScore); // Total score should be 0 for incorrect answer
        verify(userRepository, times(1)).findById(1L);
        verify(examRepository, times(1)).save(any(Exam.class));
        verify(userAnswerRepository, times(1)).save(any(UserAnswer.class));
    }
    @Test
    void testGetExamWithSufficientQuestions() {
        // Mocking the question repository
        List<JavaQuizQuestion> questions = Arrays.asList(new JavaQuizQuestion(), new JavaQuizQuestion(), new JavaQuizQuestion());
        when(questionRepository.findByDifficultyLevelLessThanEqual(1)).thenReturn(questions);

        // Mock user repository
        User mockUser = new User();
        mockUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Mock exam section repository
        List<ExamSection> mockSections = Arrays.asList(new ExamSection(), new ExamSection(), new ExamSection());
        when(examSectionRepository.saveAll(anyList())).thenReturn(mockSections);

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
        when(examRepository.findBySessionId("session123")).thenReturn(mockExam);

        // Test method
        Exam result = examService.getSubmittedExamBySessionId("session123");

        assertNotNull(result);
        verify(examRepository, times(1)).findBySessionId("session123");
    }
}