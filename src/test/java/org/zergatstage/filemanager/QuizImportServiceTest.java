package org.zergatstage.filemanager;

import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import org.zergatstage.model.Question;
import org.zergatstage.repository.QuestionRepository;
import org.zergatstage.services.validation.QuestionValidator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizImportServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuestionValidator questionValidator;

    @Mock
    private MultipartFile file;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private QuizImportService quizImportService;

    @BeforeEach
    void setUp() {
        quizImportService = new QuizImportService(questionRepository, objectMapper, questionValidator);
    }

    @Test
    void shouldImportValidQuizQuestions() throws IOException {
        // Given
        String validJson = """
            [
                {
                    "questionType": "CODE",
                    "questionHeader": "What is Java?",
                    "questionText": "Explain Java in short.",
                    "difficultyLevel": 1,
                    "correctAnswers": ["A programming language"],
                    "correctAnswerExplanation": "Java is a high-level programming language.",
                    "points": 5,
                    "choices": ["A car", "A fruit", "A programming language"]
                }
            ]
        """;

        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenReturn(validJson.getBytes(StandardCharsets.UTF_8));

        // When
        quizImportService.importQuizQuestions(file);

        // Then
        ArgumentCaptor<List<Question>> captor = ArgumentCaptor.forClass(List.class);
        verify(questionRepository, times(1)).saveAll(captor.capture());

        List<Question> savedQuestions = captor.getValue();
        assertEquals(1, savedQuestions.size());
        assertEquals("What is Java?", savedQuestions.get(0).getQuestionHeader());
        assertEquals(List.of("A programming language"), savedQuestions.get(0).getCorrectAnswers());
    }

    @Test
    void shouldHandleLegacyCorrectAnswerField() throws IOException {
        // Given (Old JSON format with single correctAnswer field)
        String legacyJson = """
            [
                {
                    "questionType": "CODE",
                    "questionHeader": "What is Java?",
                    "questionText": "Explain Java in short.",
                    "difficultyLevel": 1,
                    "correctAnswer": "A programming language",
                    "correctAnswerExplanation": "Java is a high-level programming language.",
                    "points": 5,
                    "choices": ["A car", "A fruit", "A programming language"]
                }
            ]
        """;

        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenReturn(legacyJson.getBytes(StandardCharsets.UTF_8));

        // When
        quizImportService.importQuizQuestions(file);

        // Then
        ArgumentCaptor<List<Question>> captor = ArgumentCaptor.forClass(List.class);
        verify(questionRepository).saveAll(captor.capture());

        List<Question> savedQuestions = captor.getValue();
        assertEquals(1, savedQuestions.size());
        assertEquals("What is Java?", savedQuestions.get(0).getQuestionHeader());
        assertEquals(List.of("A programming language"), savedQuestions.get(0).getCorrectAnswers());
    }

    @Test
    void shouldThrowExceptionForMalformedJson() throws IOException {
        // Given (Invalid JSON structure)
        String invalidJson = """
            [
                {
                    "questionType": "CODE",
                    "questionHeader": "What is Java?",
                    "questionText": "Explain Java in short."
                    "difficultyLevel": 1
                }
            ]
        """;

        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenReturn(invalidJson.getBytes(StandardCharsets.UTF_8));

        // When & Then
        assertThrows(IOException.class, () -> quizImportService.importQuizQuestions(file));
        verify(questionRepository, never()).saveAll(any());
    }

    @Test
    void shouldThrowExceptionForEmptyFile() {
        // Given
        when(file.isEmpty()).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> quizImportService.importQuizQuestions(file));
        verify(questionRepository, never()).saveAll(any());
    }

    @Test
    void shouldValidateQuestionsBeforeSaving() throws IOException {
        // Given
        String validJson = """
            [
                {
                    "questionType": "SIMPLE",
                    "questionHeader": "What is OOP?",
                    "questionText": "Explain OOP concepts.",
                    "difficultyLevel": 2,
                    "correctAnswers": ["Encapsulation", "Inheritance"],
                    "correctAnswerExplanation": "OOP stands for Object-Oriented Programming.",
                    "points": 10,
                    "choices": ["Encapsulation", "Inheritance", "Compilation"]
                }
            ]
        """;

        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenReturn(validJson.getBytes(StandardCharsets.UTF_8));

        // When
        quizImportService.importQuizQuestions(file);

        // Then
        ArgumentCaptor<List<Question>> captor = ArgumentCaptor.forClass(List.class);
        verify(questionRepository, times(1)).saveAll(captor.capture());

        List<Question> savedQuestions = captor.getValue();
        assertEquals(1, savedQuestions.size());
        assertEquals("What is OOP?", savedQuestions.get(0).getQuestionHeader());

        // Ensure validation was called
        verify(questionValidator, times(1)).validate(any(Question.class));
    }
}
