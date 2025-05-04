package org.zergatstage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zergatstage.model.Question;
import org.zergatstage.repository.QuestionRepository;

import java.util.Collections;
import java.util.List;

/**
 * @author father
 */
@Service
public class JavaQuizServiceImpl implements JavaQuizService{

    @Autowired
    private final QuestionRepository repository;

    public JavaQuizServiceImpl(QuestionRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<Question> getRandomQuestionsByLevel(int random, int level) {
        List<Question> questionsAll = repository.findAll();
        Collections.shuffle(questionsAll);
        return questionsAll.subList(0, random);
    }
}
