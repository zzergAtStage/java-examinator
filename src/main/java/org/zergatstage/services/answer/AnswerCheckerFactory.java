package org.zergatstage.services.answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.zergatstage.model.AnswerFormat;

import java.util.Map;

/** Provides the choice of question checker depending on the question type
 * @author father
 */
@Component
@ComponentScan("org.zergatstage.services.answer")
public class AnswerCheckerFactory {
  private final Map<String, AnswerChecker> answerCheckers;

  @Autowired
  public AnswerCheckerFactory(Map<String, AnswerChecker> answerCheckers) {
    this.answerCheckers = answerCheckers;
  }
  public AnswerChecker getAnswerCheckerByAnswersType(AnswerFormat answerFormat){
    AnswerChecker checker = answerCheckers.get(answerFormat.toString().toLowerCase() + "AnswerChecker");
    if (checker == null) {
      throw new IllegalArgumentException("Unsupported question type: " + answerFormat);
    }
    return checker;
  }
}
