package org.zergatstage.services.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zergatstage.exceptions.QuestionValidationException;
import org.zergatstage.model.AnswerType;
import org.zergatstage.model.JavaQuizQuestion;
import org.zergatstage.model.QuestionType;
import org.zergatstage.services.ExamService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.zergatstage.model.QuestionType.SIMPLE;

@ExtendWith(MockitoExtension.class)
class QuestionValidatorTest {

    @Mock
    private ExamService examService;

    private QuestionValidator questionValidator;

    @BeforeEach
    void setUp() {
        questionValidator = new QuestionValidator(examService);
    }

    @Test
    void validate_ValidQuestion_ShouldPassValidation() {
        // Arrange
        JavaQuizQuestion question = JavaQuizQuestion.builder()
                .questionHeader("What is Java?")
                .questionType(SIMPLE)
                .choices(Arrays.asList("A programming language", "A coffee brand"))
                .correctAnswers(Collections.singletonList("A programming language"))
                .points(10)
                .build();

        doNothing().when(examService).ensureQuestionIsUnique(any(JavaQuizQuestion.class));

        // Act & Assert
        assertDoesNotThrow(() -> questionValidator.validate(question));
        verify(examService).ensureQuestionIsUnique(question);
    }

    @Test
    void validate_MissingHeader_ShouldThrowException() {
        // Arrange
        JavaQuizQuestion question = JavaQuizQuestion.builder()
                .questionType(SIMPLE)
                .choices(Arrays.asList("Choice 1", "Choice 2"))
                .correctAnswers(Collections.singletonList("Choice 1"))
                .points(10)
                .build();

        // Act & Assert
        QuestionValidationException exception = assertThrows(
                QuestionValidationException.class,
                () -> questionValidator.validate(question)
        );
        assertTrue(exception.getErrors().contains("Question header is missing"));
    }

    @Test
    void validate_MissingChoices_ShouldThrowException() {
        // Arrange
        JavaQuizQuestion question = JavaQuizQuestion.builder()
                .questionHeader("Test Question")
                .questionType(SIMPLE)
                .correctAnswers(Collections.singletonList("Choice 1"))
                .points(10)
                .build();

        // Act & Assert
        QuestionValidationException exception = assertThrows(
                QuestionValidationException.class,
                () -> questionValidator.validate(question)
        );
        assertTrue(exception.getErrors().contains("Choices are missing"));
    }

    @Test
    void validate_CodeQuestionWithoutText_ShouldThrowException() {
        // Arrange
        JavaQuizQuestion question = JavaQuizQuestion.builder()
                .questionHeader("Code Question")
                .questionType(QuestionType.CODE)
                .choices(Arrays.asList("Choice 1", "Choice 2"))
                .correctAnswers(Collections.singletonList("Choice 1"))
                .points(10)
                .build();

        // Act & Assert
        QuestionValidationException exception = assertThrows(
                QuestionValidationException.class,
                () -> questionValidator.validate(question)
        );
        assertTrue(exception.getErrors().contains("Code question must have question text"));
    }

    @Test
    void validate_CorrectAnswerNotInChoices_ShouldThrowException() {
        // Arrange
        JavaQuizQuestion question = JavaQuizQuestion.builder()
                .questionHeader("Test Question")
                .questionType(SIMPLE)
                .choices(Arrays.asList("Choice 1", "Choice 2"))
                .correctAnswers(Collections.singletonList("Choice 3"))
                .points(10)
                .build();

        // Act & Assert
        QuestionValidationException exception = assertThrows(
                QuestionValidationException.class,
                () -> questionValidator.validate(question)
        );
        String errMessageToTest = "Correct answer '" + question.getCorrectAnswers() + "' is not among the provided choices.";
        assertTrue(exception.getErrors().stream()
                .allMatch( (e) -> e.contains("Correct answer")));
    }

    @Test
    void validate_ZeroPoints_ShouldThrowException() {
        // Arrange
        JavaQuizQuestion question = JavaQuizQuestion.builder()
                .questionHeader("Test Question")
                .questionType(SIMPLE)
                .choices(Arrays.asList("Choice 1", "Choice 2"))
                .correctAnswers(Collections.singletonList("Choice 1"))
                .points(0)
                .build();

        // Act & Assert
        QuestionValidationException exception = assertThrows(
                QuestionValidationException.class,
                () -> questionValidator.validate(question)
        );
        assertTrue(exception.getErrors().contains("Points must be greater than 0"));
    }

    @Test
    void validate_MultipleCorrectAnswers_ShouldSetMultipleAnswerType() {
        // Arrange
        JavaQuizQuestion question = JavaQuizQuestion.builder()
                .questionHeader("Test Question")
                .questionType(SIMPLE)
                .choices(Arrays.asList("Choice 1", "Choice 2", "Choice 3"))
                .correctAnswers(Arrays.asList("Choice 1", "Choice 2"))
                .points(10)
                .build();

        doNothing().when(examService).ensureQuestionIsUnique(any(JavaQuizQuestion.class));

        // Act
        questionValidator.validate(question);

        // Assert
        assertEquals(AnswerType.MULTIPLE, question.getTypeOfAnswer());
    }

    @Test
    void validate_SingleCorrectAnswer_ShouldSetSingleAnswerType() {
        // Arrange
        JavaQuizQuestion question = JavaQuizQuestion.builder()
                .questionHeader("Test Question")
                .questionType(SIMPLE)
                .choices(Arrays.asList("Choice 1", "Choice 2"))
                .correctAnswers(Collections.singletonList("Choice 1"))
                .points(10)
                .build();

        doNothing().when(examService).ensureQuestionIsUnique(any(JavaQuizQuestion.class));

        // Act
        questionValidator.validate(question);

        // Assert
        assertEquals(AnswerType.SINGLE, question.getTypeOfAnswer());
    }

    @Test
    void validate_MultipleValidationErrors_ShouldCollectAllErrors() {
        // Arrange
        JavaQuizQuestion question = JavaQuizQuestion.builder()
                .questionType(QuestionType.CODE) // Missing questionText for CODE type
                .points(0) // Invalid points
                .build(); // Missing choices and correctAnswers

        // Act & Assert
        QuestionValidationException exception = assertThrows(
                QuestionValidationException.class,
                () -> questionValidator.validate(question)
        );

        List<String> errors = exception.getErrors();
        assertEquals(5, errors.size());
        assertTrue(errors.contains("Question header is missing"));
        assertTrue(errors.contains("Choices are missing"));
        assertTrue(errors.contains("Code question must have question text"));
        assertTrue(errors.contains("Correct answer is missing"));
        assertTrue(errors.contains("Points must be greater than 0"));
    }
}