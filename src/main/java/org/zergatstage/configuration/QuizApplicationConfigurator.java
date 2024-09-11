package org.zergatstage.configuration;

import lombok.Setter;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.zergatstage.services.QuestionLoaderService;

/**
 * @author father
 */
@Component
public class QuizApplicationConfigurator implements ApplicationRunner {

    private final QuestionLoaderService questionLoaderService;

    public QuizApplicationConfigurator(QuestionLoaderService questionLoaderService) {
        this.questionLoaderService = questionLoaderService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        questionLoaderService.loadQuestionsFromFile();
    }
}
