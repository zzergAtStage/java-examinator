package org.zergatstage.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zergatstage.model.JavaQuizQuestion;
import org.zergatstage.model.QuestionType;
import org.zergatstage.repository.JavaQuizRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author father
 */
@Service
public class QuizImportService {

    @Autowired
    private JavaQuizRepository javaQuizRepository;

    @Autowired
    private ObjectMapper objectMapper; // Jackson ObjectMapper for JSON parsing

    /**
     * Imports quiz questions from a JSON file.
     * @param file Multipart file uploaded containing JSON data
     * @throws IOException if an error occurs during file parsing or saving
     */
    public void importQuizQuestions(MultipartFile file) throws IOException {
        // Check if the file is empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Read the content of the file into a string
        String jsonContent = new String(file.getBytes());

        // Convert JSON content to a list of JavaQuizQuestion objects
        JavaQuizQuestion[] questionsArray = objectMapper.readValue(jsonContent, JavaQuizQuestion[].class);
        List<JavaQuizQuestion> questionsList = Arrays.asList(questionsArray);

        // Validate and save each question
        for (JavaQuizQuestion question : questionsList) {
            validateQuestion(question);
            javaQuizRepository.save(question);
        }
    }

    /**
     * Validates a single JavaQuizQuestion object.
     * @param question JavaQuizQuestion object to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateQuestion(JavaQuizQuestion question) {
        if (question.getQuestionHeader() == null || question.getQuestionHeader().isEmpty()) {
            throw new IllegalArgumentException("Question text is missing");
        }
        if (question.getQuestionType() == QuestionType.CODE &&
                (question.getQuestionText() == null || question.getQuestionText().isEmpty())
                ) {
            throw new IllegalArgumentException("Question formed incorrect");
        }
        if (question.getCorrectAnswer() == null || question.getCorrectAnswer().isEmpty()) {
            throw new IllegalArgumentException("Correct answer is missing");
        }
        if (question.getChoices() == null || question.getChoices().isEmpty()) {
            throw new IllegalArgumentException("Choices are missing");
        }
        if (question.getPoints() <= 0) {
            throw new IllegalArgumentException("Points must be greater than 0");
        }
    }
}
