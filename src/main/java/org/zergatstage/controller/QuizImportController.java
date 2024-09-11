package org.zergatstage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zergatstage.services.QuizImportService;

/**
 * @author father
 */
@RestController
@RequestMapping("/api/quiz")
public class QuizImportController {

    @Autowired
    private QuizImportService quizImportService;

    @PostMapping("/import")
    public ResponseEntity<String> importQuestionsFromFile(@RequestParam(name = "quiz") MultipartFile file) {
        try {
            // Logging the file details for debugging purposes
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File is empty or not uploaded properly.");
            }
            quizImportService.importQuizQuestions(file);
            return ResponseEntity.ok("Quiz questions imported successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error importing quiz questions: " + e.getMessage());
        }
    }
}
