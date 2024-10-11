package org.zergatstage.filemanager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.zergatstage.model.JavaQuizQuestion;
import org.zergatstage.repository.JavaQuizRepository;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Loads pre initialized  questions
 *
 * @author father
 */
@Service
@RequiredArgsConstructor
@AllArgsConstructor
@Slf4j
public class QuestionFileManagementService {
  @Value("${application.questions.file}")
  private String fileName;

  @Value("${application.questions.import.enabled}")
  private Boolean importEnabled;


  @Autowired
  private JavaQuizRepository javaQuizRepository;
  @Autowired
  private QuizImportService quizImportService;

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady() {
    if (importEnabled) loadQuestionsFromFile();
  }


  @SneakyThrows

  public void loadQuestionsFromFile() {
    log.warn("Fill DB with some questions from: {{}}", fileName);
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
    if (inputStream == null) {
      log.warn("Could not find questions file.");
      return;
    }
    ObjectMapper mapper = new ObjectMapper();
    List<JavaQuizQuestion> questions = mapper.readValue(inputStream, new TypeReference<>() {
    });
    // Validate and save each question
    for (JavaQuizQuestion question : questions) {
      quizImportService.validateQuestion(question);
    }
    javaQuizRepository.saveAll(questions); // Save all questions to the repository
  }
  // Method to save a new version of exam.json to ./data with date-time appended to the filename

// father 11.10.2024:10:44 refactor to eventListener

  public void saveQuestionsToFile() throws FileNotFoundException {
    // Fetch all questions from the repository
    List<JavaQuizQuestion> questions = javaQuizRepository.findAllWithChoices();
    // Ensure all lazy-loaded collections are initialized
    if (questions.isEmpty()) return;
    // Format current date-time to append to the filename
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    String timestamp = LocalDateTime.now().format(formatter);

    // Create the output filename with date-time appended
    String newFileName = "exam_" + timestamp + ".json";

    // Define the path where the file should be saved, inside the ./data directory
    File outputFile = Paths.get("./data", newFileName).toFile();

    // Ensure the directory exists, if not create it
    if (!outputFile.getParentFile().exists()) {
      outputFile.getParentFile().mkdirs();
    }

    // Use ObjectMapper to write the questions list to the file
    ObjectMapper mapper = new ObjectMapper();
    try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
      mapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, questions);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    log.info("Questions saved to file: {}", outputFile.getAbsolutePath());
  }


}
