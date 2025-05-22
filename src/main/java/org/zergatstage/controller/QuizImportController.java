package org.zergatstage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zergatstage.filemanager.QuizImportService;

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

    @DeleteMapping("/delete")
    public ResponseEntity<?> dropImportedQuestions(){
        quizImportService.dropImport();
        return ResponseEntity.status(HttpStatus.OK).body("That's fine, that's ok...");
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteQuestionById(@PathVariable("id") Long id){
        quizImportService.deleteQuestionById(id);
        return ResponseEntity.ok(String.format("Question with id = %s deleted",id));
    }
}
