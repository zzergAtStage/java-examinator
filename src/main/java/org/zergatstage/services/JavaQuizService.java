package org.zergatstage.services;

import org.zergatstage.model.JavaQuizQuestion;

import java.util.List;

/**
 * @author father
 */
public interface JavaQuizService {
    List<JavaQuizQuestion> getRandomQuestionsByLevel(int random, int level);


}
