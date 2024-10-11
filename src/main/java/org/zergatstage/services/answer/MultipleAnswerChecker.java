package org.zergatstage.services.answer;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

/**
 * @author father
 */
@Component("multipleAnswerChecker")
public class MultipleAnswerChecker implements AnswerChecker {
  @Override
  public boolean isCorrect(List<String> correctAnswers, List<String> userAnswers) {
    return new HashSet<>(correctAnswers).containsAll(userAnswers) && new HashSet<>(userAnswers).containsAll(correctAnswers);
  }
}

