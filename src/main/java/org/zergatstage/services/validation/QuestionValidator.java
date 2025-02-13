package org.zergatstage.services.validation;

import org.springframework.stereotype.Component;
import org.zergatstage.exceptions.QuestionValidationException;
import org.zergatstage.model.AnswerType;
import org.zergatstage.model.JavaQuizQuestion;
import org.zergatstage.model.QuestionType;
import org.zergatstage.services.ExamService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuestionValidator {
    private final ExamService examService;

    public void validate(JavaQuizQuestion question) {
        List<String> errors = new ArrayList<>();

        validateBasicFields(question, errors);
        validateQuestionType(question, errors);
        validateAnswers(question, errors);
        validatePoints(question, errors);

        if (!errors.isEmpty()) {
            throw new QuestionValidationException(
                    (question.getId() != null) ? question.getId().toString() : question.getQuestionHeader()
                    , errors);
        }

        determineAnswerType(question);
        ensureUniqueness(question);
    }

    private void validateBasicFields(JavaQuizQuestion question, List<String> errors) {
        if (isBlank(question.getQuestionHeader())) {
            errors.add("Question header is missing");
        }

        if (question.getChoices() == null || question.getChoices().isEmpty()) {
            errors.add("Choices are missing");
        }
    }

    private void validateQuestionType(JavaQuizQuestion question, List<String> errors) {
        if (question.getQuestionType() == QuestionType.CODE && isBlank(question.getQuestionText())) {
            errors.add("Code question must have question text");
        }
    }

    private void validateAnswers(JavaQuizQuestion question, List<String> errors) {
        List<String> answers = question.getCorrectAnswers();
        List<String> choices = question.getChoices();
        if (answers == null || answers.isEmpty()) {
            errors.add("Correct answer is missing");
            return;
        }
        // Normalize choices to avoid object reference mismatches
        Set<String> normalizedChoices = choices.stream()
                .map(String::trim)  // Trim whitespace for robustness
                .collect(Collectors.toSet());

        // Ensure every answer exists in choices (ignoring object identity issues)
        for (String answer : answers) {
            if (!normalizedChoices.contains(answer.trim())) {
                errors.add("Correct answer '" + answer + "' is not among the provided choices.");
            }
        }

        if (!errors.isEmpty()) {
            throw new QuestionValidationException(
                    ((question.getId() != null) ? question.getId().toString() : question.getQuestionHeader())
                            ,errors);
        }
    }

    private void validatePoints(JavaQuizQuestion question, List<String> errors) {
        if (question.getPoints() <= 0) {
            errors.add("Points must be greater than 0");
        }
    }

    private void determineAnswerType(JavaQuizQuestion question) {
        if (question.getTypeOfAnswer() == null) {
            boolean isMultiple = question.getCorrectAnswers() != null &&
                    question.getCorrectAnswers().size() > 1;
            question.setTypeOfAnswer(isMultiple ? AnswerType.MULTIPLE : AnswerType.SINGLE);
        }
    }

    private void ensureUniqueness(JavaQuizQuestion question) {
        examService.ensureQuestionIsUnique(question);
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}