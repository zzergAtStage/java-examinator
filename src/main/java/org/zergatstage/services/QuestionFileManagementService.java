package org.zergatstage.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zergatstage.model.JavaQuizQuestion;
import org.zergatstage.repository.JavaQuizRepository;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

/**
 * Loads pre initialized  questions
 *
 * @author father
 */
@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class QuestionFileManagementService {
    @Value("${questions.file}")
    private String fileName;
    @Autowired
    private JavaQuizRepository javaQuizRepository;


    @SneakyThrows
//    @PostConstruct //call this method after all beans are initialized
    public void loadQuestionsFromFile() {
        Logger.getAnonymousLogger().warning("Fill DB with some questions from: {" + fileName + "}");
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            Logger.getAnonymousLogger().warning("Could not find questions file.");
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        List<JavaQuizQuestion> questions = mapper.readValue(inputStream, new TypeReference<>() {
        });

        javaQuizRepository.saveAll(questions); // Save all questions to the repository
    }
    // Method to save a new version of exam.json to ./data with date-time appended to the filename
    @SneakyThrows
    @PreDestroy
    public void saveQuestionsToFile() {
        // Fetch all questions from the repository
        List<JavaQuizQuestion> questions = javaQuizRepository.findAllWithChoices();
        // Ensure all lazy-loaded collections are initialized

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
        }

        Logger.getAnonymousLogger().info("Questions saved to file: " + outputFile.getAbsolutePath());
    }


}
