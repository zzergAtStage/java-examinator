package org.zergatstage.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class QuestionValidationException extends RuntimeException {
  private final String questionId;
  private final List<String> errors;

  public QuestionValidationException(String questionId, List<String> errors) {
    super(String.format("Validation failed for question %s: %s", questionId, String.join(", ", errors)));
    this.questionId = questionId;
    this.errors = errors;
  }

}