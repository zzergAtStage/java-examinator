package org.zergatstage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zergatstage.DTO.ExamSubmissionDTO;
import org.zergatstage.DTO.ResponseDTO;
import org.zergatstage.model.Exam;
import org.zergatstage.model.JavaQuizQuestion;
import org.zergatstage.model.User;
import org.zergatstage.services.ExamService;
import org.zergatstage.services.UserService;

/**
 * @author father
 */
@RestController
@RequestMapping("/api/v1/exam")
public class ExamController {

    private final ExamService examService;
    private final UserService userService;

    public ExamController(ExamService examService, UserService userService) {
        this.examService = examService;
        this.userService = userService;
    }

    @GetMapping("/generate")
    public ResponseEntity<Exam> generateQuiz(@RequestParam int difficulty, int numberQuestions){
        return ResponseEntity.ok(examService.getExam(difficulty, numberQuestions));
    }

    @GetMapping("/answered")
    public ResponseEntity<Exam> getLastAnsweredExam(@RequestParam String sessionId){
        Exam exam = examService.getSubmittedExamBySessionId(sessionId);
        return ResponseEntity.ok(exam);
    }
    @PostMapping("/submit")
    public ResponseEntity<Integer> submitExam(@RequestBody ExamSubmissionDTO submission) {
        int totalScore = examService.gradeExam(submission);
        return ResponseEntity.ok(totalScore); // Return the total score of the exam
    }

    @PostMapping("/user")
    public ResponseEntity<User> registerUser(@RequestParam String username){
        User user = userService.getUserByUsername(username);
        if (user == null) {
            user = userService.registerUser(username);
        }
        assert user != null;
        return ResponseEntity.ok(user);
    }

    @PostMapping("/addQuestion")
    public ResponseEntity<ResponseDTO> addQuestion(@RequestBody JavaQuizQuestion javaQuizQuestion) {
        ResponseDTO responseDTO = new ResponseDTO();
            // Save the question to the database
            examService.saveQuestion(javaQuizQuestion);
            //TODO 8.10.2024 father: Refactor this.
            // Return a success message
            responseDTO.setBusinessMessage("Success");
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    @GetMapping("/question/{id}")
    public ResponseEntity<JavaQuizQuestion> getOneQuestionById(@PathVariable("id") Long id) {
        JavaQuizQuestion javaQuizQuestion = examService.getQuestionById(id);
        return new ResponseEntity<>(javaQuizQuestion, HttpStatus.OK);
    }

    @PutMapping("/update-question/{id}")
    public ResponseEntity<ResponseDTO> updateQuestion(@PathVariable("id") Long id, @RequestBody JavaQuizQuestion question) {
        examService.updateQuestion(id,question);
        return new ResponseEntity<>(ResponseDTO.builder().businessMessage("Updated").build(), HttpStatus.OK);
    }
}
