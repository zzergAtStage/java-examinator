package org.zergatstage.services;

import org.zergatstage.model.Question;

import java.util.List;

/**
 * @author father
 */
public interface JavaQuizService {
    List<Question> getRandomQuestionsByLevel(int random, int level);


}
