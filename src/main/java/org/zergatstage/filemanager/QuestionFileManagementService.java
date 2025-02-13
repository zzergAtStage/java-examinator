package org.zergatstage.filemanager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zergatstage.model.JavaQuizQuestion;
import org.zergatstage.repository.JavaQuizRepository;
import org.zergatstage.services.validation.QuestionValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service responsible for managing question files, including loading initial questions
 * and periodic backups of the question database.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionFileManagementService {
  private static final String BACKUP_DIRECTORY = "./data/backups";
  private static final String BACKUP_FILE_PREFIX = "exam_";
  private static final String BACKUP_FILE_EXTENSION = ".json";
  private static final DateTimeFormatter TIMESTAMP_FORMATTER =
          DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

  @Value("${application.questions.file}")
  private String initialQuestionsFile;

  @Value("${application.questions.import.enabled}")
  private Boolean importEnabled;

  @Value("${application.questions.backup.enabled:true}")
  private Boolean backupEnabled;

  private final JavaQuizRepository javaQuizRepository;
  private final QuizImportService quizImportService;
  private final ObjectMapper objectMapper;
  private final QuestionValidator questionValidator;

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady() {
    if (importEnabled) {
      try {
        loadInitialQuestions();
      } catch (Exception e) {
        log.error("Failed to load initial questions", e);
      }
    }
  }

  /**
   * Loads initial questions from the configured file.
   * @throws IOException if there's an error reading the file
   */
  @Transactional
  public void loadInitialQuestions() throws IOException {
    if (initialQuestionsFile == null || initialQuestionsFile.trim().isEmpty()) return;
    log.info("Loading initial questions from: {}", initialQuestionsFile);

    try (InputStream inputStream = getClass().getClassLoader()
            .getResourceAsStream(initialQuestionsFile)) {
      if (inputStream == null) {
        throw new FileNotFoundException("Questions file not found: " + initialQuestionsFile);
      }

      List<JavaQuizQuestion> questions = objectMapper.readValue(
              inputStream,
              new TypeReference<List<JavaQuizQuestion>>() {}
      );

      AtomicInteger successCount = new AtomicInteger(0);
      questions.forEach(question -> {
        try {
          questionValidator.validate(question);
          javaQuizRepository.save(question);
          successCount.incrementAndGet();
        } catch (Exception e) {
          log.error("Failed to import question: {}", question, e);
        }
      });

      log.info("Successfully imported {} out of {} questions",
              successCount.get(), questions.size());
    }
  }

  /**
   * Performs automated backup of questions every day at midnight.
   */
  @Scheduled(cron = "0 0 0 * * ?")
  @Transactional(readOnly = true)
  public void scheduledBackup() {
    if (backupEnabled) {
      try {
        createBackup();
        log.info("Scheduled backup completed successfully");
      } catch (Exception e) {
        log.error("Scheduled backup failed", e);
      }
    }
  }

  /**
   * Creates a backup of all questions in the database.
   * @return Path to the created backup file
   * @throws IOException if there's an error writing the backup
   */
  public Path createBackup() throws IOException {
    List<JavaQuizQuestion> questions = javaQuizRepository.findAllWithChoices();
    if (questions.isEmpty()) {
      log.warn("No questions found to backup");
      return null;
    }

    String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    String filename = BACKUP_FILE_PREFIX + timestamp + BACKUP_FILE_EXTENSION;
    Path backupDir = Paths.get(BACKUP_DIRECTORY);
    Path backupFile = backupDir.resolve(filename);

    Files.createDirectories(backupDir);

    objectMapper.writerWithDefaultPrettyPrinter()
            .writeValue(backupFile.toFile(), questions);

    log.info("Created backup with {} questions at: {}",
            questions.size(), backupFile);

    cleanupOldBackups(backupDir);

    return backupFile;
  }

  /**
   * Keeps only the last 7 backup files, deleting older ones.
   */
  private void cleanupOldBackups(Path backupDir) throws IOException {
    if (!Files.exists(backupDir)) return;

    List<Path> backupFiles = Files.list(backupDir)
            .filter(path -> path.getFileName().toString().startsWith(BACKUP_FILE_PREFIX))
            .sorted((p1, p2) -> p2.getFileName().toString().compareTo(p1.getFileName().toString()))
            .toList();

    if (backupFiles.size() > 7) {
      for (int i = 7; i < backupFiles.size(); i++) {
        Files.delete(backupFiles.get(i));
        log.debug("Deleted old backup: {}", backupFiles.get(i));
      }
    }
  }
}