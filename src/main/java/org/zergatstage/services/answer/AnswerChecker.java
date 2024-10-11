package org.zergatstage.services.answer;

import java.util.List;

/** Provide single method to check answers list
 * @author father
 */
public interface AnswerChecker {
  boolean isCorrect(List<String> correctAnswers, List<String> userAnswers);
}