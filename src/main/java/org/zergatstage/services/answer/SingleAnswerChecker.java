package org.zergatstage.services.answer;

import org.springframework.stereotype.Component;

import java.util.List;


@Component("singleAnswerChecker")
public class SingleAnswerChecker implements AnswerChecker {
  @Override
  public boolean isCorrect(List<String> correctAnswers, List<String> userAnswers) {
    return !userAnswers.isEmpty() && correctAnswers.contains(userAnswers.get(0));
  }
}
