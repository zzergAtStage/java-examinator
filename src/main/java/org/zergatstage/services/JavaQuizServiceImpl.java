package org.zergatstage.services;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zergatstage.model.JavaQuizQuestion;
import org.zergatstage.repository.JavaQuizRepository;

import java.util.Collections;
import java.util.List;

/**
 * @author father
 */
@Service
public class JavaQuizServiceImpl implements JavaQuizService{

    @Autowired
    private final JavaQuizRepository repository;

    public JavaQuizServiceImpl(JavaQuizRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<JavaQuizQuestion> getRandomQuestionsByLevel(int random, int level) {
        List<JavaQuizQuestion> questionsAll = repository.findAll();
        Collections.shuffle(questionsAll);
        return questionsAll.subList(0, random);
    }
}
