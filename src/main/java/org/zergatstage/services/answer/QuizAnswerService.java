package org.zergatstage.services.answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zergatstage.model.JavaQuizQuestion;
import org.zergatstage.model.Questions;

/** Calls a correct checker and call checks
 * @author father
 */
@Service
public class QuizAnswerService {
  private final AnswerCheckerFactory checkerFactory;

  @Autowired
  public QuizAnswerService(AnswerCheckerFactory checkerFactory) {
    this.checkerFactory = checkerFactory;
  }

  /**
   * Checks if a user's answers to a specific question are correct.
   *
   * @param question   The JavaQuizQuestion entity containing correct answers.
   * @param questions The UserAnswer entity containing user's answers.
   * @return true if user's answers are correct, false otherwise.
   */
  public boolean isAnswerCorrect(JavaQuizQuestion question, Questions questions) {
    AnswerChecker checker = checkerFactory.getAnswerCheckerByAnswersType(question.getTypeOfAnswer());
    return checker.isCorrect(question.getCorrectAnswers(), questions.getUserAnswers());
  }
}
