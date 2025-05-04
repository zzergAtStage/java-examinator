package org.zergatstage.filemanager;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.zergatstage.model.AnswerFormat;
import org.zergatstage.model.Option;
import org.zergatstage.model.Question;
import org.zergatstage.model.QuestionType;
import org.zergatstage.model.dto.QuestionImportDTO;
import org.zergatstage.repository.QuestionRepository;
import org.zergatstage.services.validation.QuestionValidator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class QuizImportService {
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;
    private final QuestionValidator questionValidator;

    public QuizImportService(QuestionRepository questionRepository, ObjectMapper objectMapper, QuestionValidator questionValidator) {
        this.questionRepository = questionRepository;
        this.objectMapper = objectMapper;
        this.questionValidator = questionValidator;
    }

    @Transactional
    public void importQuizQuestions(MultipartFile file) throws IOException {
        validateFile(file);
        List<Question> questions = parseQuestions(file);
        saveQuestions(questions);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null");
        }
    }

    private List<Question> parseQuestions(MultipartFile file) throws IOException {
        String jsonContent = null;
        try (InputStream in = file.getInputStream()){
            var dtoList = objectMapper.readValue(
                    in,
                    new TypeReference<List<QuestionImportDTO>>() { }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonNode rootNode = objectMapper.readTree(jsonContent);

        List<Question> questions = new ArrayList<>();

        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                Question question = convertJsonToQuestion(node);
                questions.add(question);
            }
        } else {
            Question question = convertJsonToQuestion(rootNode);
            questions.add(question);
        }

        log.info("Parsed {} questions from the imported file.", questions.size());
        return questions;
    }

    /**
     * Converts both legacy and new JSON structures into JavaQuizQuestion objects.
     */
    private Question convertJsonToQuestion(JsonNode node) {
        Question question = new Question()
                .header(node.get("questionHeader").asText())
                .stem(node.get("questionText").asText())
                .difficulty(node.get("difficultyLevel").asInt())
                .points(node.get("points").asInt());


        // Convert question type
        if (node.has("questionType")) {
            question.questionType(QuestionType.valueOf(node.get("questionType").asText()));
        } else {
            question.questionType(QuestionType.STANDARD); // Default type
        }

        // Convert answer type (default to SINGLE if missing)
        if (node.has("typeOfAnswer")) {
            question.answerFormat(AnswerFormat.valueOf(node.get("typeOfAnswer").asText()));
        } else {
            question.answerFormat(AnswerFormat.SINGLE_CHOICE);
        }

        // Convert choices -- moved to the many 2 many relationship with Question
        if (node.has("options") && node.get("options").isArray()) {
            List<Option> options = new ArrayList<>();
            node.get("options").forEach(option -> options.add(
                    Option.builder()
                            .text(option.get("text").asText())
                            .correct(option.get("correct").asBoolean())
                            .explanation(
                                    option.get("explanation").asText())
                            .displayOrder(option.get("displayOrder").asInt())
                            .question(question)
                            .build()
                    )
            );

        }

        // Convert correct answers: Handle legacy "correctAnswer" field
//        if (node.has("correctAnswers") && node.get("correctAnswers").isArray()) {
//            List<String> correctAnswers = new ArrayList<>();
//            node.get("correctAnswers").forEach(answer -> correctAnswers.add(answer.asText()));
//            builder.correctAnswers(correctAnswers);
//        } else if (node.has("correctAnswer")) { // Legacy format
//            builder.correctAnswers(List.of(node.get("correctAnswer").asText().split(",")));
//            builder.typeOfAnswer(AnswerFormat.MULTIPLE);
//            log.info("Converted legacy correctAnswer '{}' to correctAnswers list.", node.get("correctAnswer").asText());
//        }

        // Ignore legacy "id" field to avoid inserting existing IDs
        return question;
    }

    private void saveQuestions(List<Question> questions) {
        questions.forEach(questionValidator::validate);
        questionRepository.saveAll(questions);
    }

    public void dropImport() {
        questionRepository.deleteAll();
    }
}