package org.zergatstage.services.answer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.zergatstage.model.AnswerType;
import org.zergatstage.model.JavaQuizQuestion;
import org.zergatstage.model.QuestionType;
import org.zergatstage.model.Questions;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ComponentScan({"org.zergatstage.services.answer"})
class AnswerCheckerServiceTest {
  @Autowired
  private QuizAnswerService quizAnswerService;


  private JavaQuizQuestion singleAnswerQuestion;
  private JavaQuizQuestion multipleAnswerQuestion;

  @Mock
  private Questions userSingleAnswer;
  @Mock
  private Questions userMultipleAnswer;

  @BeforeEach
  void setup() {
    // Set up a single-answer question and corresponding user answer
    singleAnswerQuestion = JavaQuizQuestion.builder()
            .id(1L)
            .points(5)
            .questionType(QuestionType.SIMPLE)
            .choices(List.of("Answer A", "Answer C", "Answer B", "Answer D"))
            .correctAnswer("Answer A")
            .questionHeader("Simple SINGLE-answer question")
            .typeOfAnswer(AnswerType.SINGLE)
            .difficultyLevel(1)
            .build();
    multipleAnswerQuestion = JavaQuizQuestion.builder()
            .id(1L)
            .points(5)
            .questionType(QuestionType.SIMPLE)
            .choices(List.of("Answer A", "Answer C", "Answer B", "Answer D"))
            .correctAnswers(List.of("Answer A", "Answer B"))
            .questionHeader("Simple MULTI-answer question")
            .typeOfAnswer(AnswerType.MULTIPLE)
            .difficultyLevel(1)
            .build();

    when(userSingleAnswer.getUserAnswers()).thenReturn(List.of("Answer A"));
    when(userMultipleAnswer.getUserAnswers()).thenReturn(Arrays.asList("Answer A", "Answer B"));
  }
  @Test
  void testSingleAnswerChecker() {
    boolean isCorrect = quizAnswerService.isAnswerCorrect(singleAnswerQuestion, userSingleAnswer);
    assertTrue(isCorrect, "Single-answer question should be marked correct when answers match");

    // Test incorrect single answer
    when(userSingleAnswer.getUserAnswers()).thenReturn(Arrays.asList("Wrong Answer"));
    boolean isIncorrect = quizAnswerService.isAnswerCorrect(singleAnswerQuestion, userSingleAnswer);
    assertFalse(isIncorrect, "Single-answer question should be marked incorrect when answers do not match");
  }
  @Test
  void testMultipleAnswerChecker() {
    boolean isCorrect = quizAnswerService.isAnswerCorrect(multipleAnswerQuestion, userMultipleAnswer);
    assertTrue(isCorrect, "Multiple-answer question should be marked correct when all answers match");

    // Test incorrect multiple answer
    when(userMultipleAnswer.getUserAnswers()).thenReturn(Arrays.asList("Answer A", "Answer 4"));
    boolean isIncorrect = quizAnswerService.isAnswerCorrect(multipleAnswerQuestion, userMultipleAnswer);
    assertFalse(isIncorrect, "Multiple-answer question should be marked incorrect when answers do not fully match");
  }

}