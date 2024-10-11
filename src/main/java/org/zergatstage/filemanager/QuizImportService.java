package org.zergatstage.filemanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.zergatstage.model.AnswerType;
import org.zergatstage.model.JavaQuizQuestion;
import org.zergatstage.model.QuestionType;
import org.zergatstage.repository.JavaQuizRepository;
import org.zergatstage.services.ExamService;

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

    @Autowired
    private ExamService examService;
    /**
     * Imports quiz questions from a JSON file.
     * @param file Multipart file uploaded containing JSON data
     * @throws IOException if an error occurs during file parsing or saving
     */
    @Transactional
    public void importQuizQuestions(MultipartFile file) throws IOException {
        // Check if the file is empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Read the content of the file into a string
        String jsonContent = new String(file.getBytes());

        // Convert JSON content to a list of JavaQuizQuestion objects
        JavaQuizQuestion[] questionsArray = objectMapper.readValue(jsonContent, JavaQuizQuestion[].class);

      // Validate and save each question
        for (JavaQuizQuestion question : questionsArray) {
            validateQuestion(question);
            javaQuizRepository.save(question);
        }
    }

    /**
     * Validates a single JavaQuizQuestion object.
     * @param question JavaQuizQuestion object to validate
     * @throws IllegalArgumentException if validation fails
     */
    public void validateQuestion(JavaQuizQuestion question) {
        if (question.getQuestionHeader() == null || question.getQuestionHeader().isEmpty()) {
            throw new IllegalArgumentException("Question text is missing (" + question.getId() + ")");
        }
        if (question.getQuestionType() == QuestionType.CODE &&
                (question.getQuestionText() == null || question.getQuestionText().isEmpty())
                ) {
            throw new IllegalArgumentException("Question formed incorrect (" + question.getId() + ")");
        }
        if (question.getTypeOfAnswer() == null) {
            question.setTypeOfAnswer(AnswerType.SINGLE);
            if (question.getCorrectAnswers().size() > 1) question.setTypeOfAnswer(AnswerType.MULTIPLE);
        }

        List<String> answers = question.getCorrectAnswers();
        if (answers.isEmpty()) {
            throw new IllegalArgumentException("Correct answer is missing (" + question.getId() + ")");
        }
        if (question.getChoices() == null || question.getChoices().isEmpty()) {
            throw new IllegalArgumentException("Choices are missing(" + question.getId() + ")");
        }
        if (question.getPoints() <= 0) {
            throw new IllegalArgumentException("Points must be greater than 0");
        }

        examService.ensureQuestionIsUnique(question);
    }
}
