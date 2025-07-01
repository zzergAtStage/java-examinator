package org.zergatstage.services.answer;

import org.springframework.stereotype.Component;

import java.util.List;

@Component("inputAnswerChecker")
public class InputAnswerChecker implements AnswerChecker{
    @Override
    public boolean isCorrect(List<String> correctAnswers, List<String> userAnswers) {
        return !userAnswers.isEmpty() && correctAnswers.contains(userAnswers.get(0));
    }
}
