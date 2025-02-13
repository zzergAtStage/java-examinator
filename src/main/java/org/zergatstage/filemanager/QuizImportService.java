package org.zergatstage.filemanager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.zergatstage.model.AnswerType;
import org.zergatstage.model.JavaQuizQuestion;
import org.zergatstage.model.QuestionType;
import org.zergatstage.repository.JavaQuizRepository;
import org.zergatstage.services.validation.QuestionValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class QuizImportService {
    private final JavaQuizRepository javaQuizRepository;
    private final ObjectMapper objectMapper;
    private final QuestionValidator questionValidator;

    public QuizImportService(JavaQuizRepository javaQuizRepository, ObjectMapper objectMapper, QuestionValidator questionValidator) {
        this.javaQuizRepository = javaQuizRepository;
        this.objectMapper = objectMapper;
        this.questionValidator = questionValidator;
    }

    @Transactional
    public void importQuizQuestions(MultipartFile file) throws IOException {
        validateFile(file);
        List<JavaQuizQuestion> questions = parseQuestions(file);
        saveQuestions(questions);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null");
        }
    }

    private List<JavaQuizQuestion> parseQuestions(MultipartFile file) throws IOException {
        String jsonContent = new String(file.getBytes());
        JsonNode rootNode = objectMapper.readTree(jsonContent);

        List<JavaQuizQuestion> questions = new ArrayList<>();

        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                JavaQuizQuestion question = convertJsonToQuestion(node);
                questions.add(question);
            }
        } else {
            JavaQuizQuestion question = convertJsonToQuestion(rootNode);
            questions.add(question);
        }

        log.info("Parsed {} questions from the imported file.", questions.size());
        return questions;
    }

    /**
     * Converts both legacy and new JSON structures into JavaQuizQuestion objects.
     */
    private JavaQuizQuestion convertJsonToQuestion(JsonNode node) {
        JavaQuizQuestion.JavaQuizQuestionBuilder builder = JavaQuizQuestion.builder();

        builder.questionHeader(node.get("questionHeader").asText());
        builder.questionText(node.get("questionText").asText());
        builder.difficultyLevel(node.get("difficultyLevel").asInt());
        builder.correctAnswerExplanation(node.get("correctAnswerExplanation").asText());
        builder.points(node.get("points").asInt());

        // Convert question type
        if (node.has("questionType")) {
            builder.questionType(QuestionType.valueOf(node.get("questionType").asText()));
        } else {
            builder.questionType(QuestionType.SIMPLE); // Default type
        }

        // Convert answer type (default to SINGLE if missing)
        if (node.has("typeOfAnswer")) {
            builder.typeOfAnswer(AnswerType.valueOf(node.get("typeOfAnswer").asText()));
        } else {
            builder.typeOfAnswer(AnswerType.SINGLE);
        }

        // Convert choices
        if (node.has("choices") && node.get("choices").isArray()) {
            List<String> choices = new ArrayList<>();
            node.get("choices").forEach(choice -> choices.add(choice.asText()));
            builder.choices(choices);
        }

        // Convert correct answers: Handle legacy "correctAnswer" field
        if (node.has("correctAnswers") && node.get("correctAnswers").isArray()) {
            List<String> correctAnswers = new ArrayList<>();
            node.get("correctAnswers").forEach(answer -> correctAnswers.add(answer.asText()));
            builder.correctAnswers(correctAnswers);
        } else if (node.has("correctAnswer")) { // Legacy format
            builder.correctAnswers(List.of(node.get("correctAnswer").asText().split(",")));
            builder.typeOfAnswer(AnswerType.MULTIPLE);
            log.info("Converted legacy correctAnswer '{}' to correctAnswers list.", node.get("correctAnswer").asText());
        }

        // Ignore legacy "id" field to avoid inserting existing IDs
        return builder.build();
    }

    private void saveQuestions(List<JavaQuizQuestion> questions) {
        questions.forEach(questionValidator::validate);
        javaQuizRepository.saveAll(questions);
    }

}