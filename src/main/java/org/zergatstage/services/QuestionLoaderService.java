package org.zergatstage.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zergatstage.model.JavaQuizQuestion;
import org.zergatstage.repository.JavaQuizRepository;

import java.io.InputStream;
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
public class QuestionLoaderService {
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
}
