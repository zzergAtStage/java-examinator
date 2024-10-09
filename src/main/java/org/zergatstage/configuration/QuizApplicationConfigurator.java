package org.zergatstage.configuration;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.zergatstage.services.QuestionFileManagementService;

/**
 * @author father
 */
@Component
public class QuizApplicationConfigurator implements ApplicationRunner {

    private final QuestionFileManagementService questionLoaderService;

    public QuizApplicationConfigurator(QuestionFileManagementService questionLoaderService) {
        this.questionLoaderService = questionLoaderService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //questionLoaderService.loadQuestionsFromFile();
    }

}
