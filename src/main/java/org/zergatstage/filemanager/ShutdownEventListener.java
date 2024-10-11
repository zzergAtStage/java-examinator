package org.zergatstage.filemanager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

/**
 * @author father
 */
@Component
public class ShutdownEventListener implements ApplicationListener<ContextClosedEvent> {

  @Value("${application.questions.export.enabled}")
  private boolean exportEnabled;
  private final QuestionFileManagementService questionFileManagementService;
  public ShutdownEventListener(QuestionFileManagementService questionFileManagementService) {
    this.questionFileManagementService = questionFileManagementService;
  }

  @Override
  public void onApplicationEvent(ContextClosedEvent event) {
    if (!exportEnabled) return;
    try {
      questionFileManagementService.saveQuestionsToFile();
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}