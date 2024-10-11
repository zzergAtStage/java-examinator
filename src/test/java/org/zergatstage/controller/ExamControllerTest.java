package org.zergatstage.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.zergatstage.DTO.ExamSubmissionDTO;
import org.zergatstage.model.Exam;
import org.zergatstage.model.User;
import org.zergatstage.services.ExamService;
import org.zergatstage.services.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled
class ExamControllerTest {
   private MockMvc mockMvc;

    @Mock
    private ExamService examService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ExamController examController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(examController).build(); // Initializes MockMvc for standalone testing of the controller
    }



    @Test
    void testGetQuizSuccess() throws Exception {
        // Mocking the service layer response
        Exam mockExam = new Exam();
        when(examService.getExam(1, 5)).thenReturn(mockExam);

        // Simulating a GET request and verifying the response
        mockMvc.perform(get("/exam/quiz")
                        .param("difficulty", "1")
                        .param("numberQuestions", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist()); // Expecting that an exam is returned (without ID in this mock)

        // Verify interaction with service layer
        verify(examService, times(1)).getExam(1, 5);
    }

    @Test
    void testSubmitExamSuccess() throws Exception {
        // Mocking the service response (grading logic)
        when(examService.gradeExam(any(ExamSubmissionDTO.class))).thenReturn(85); // Assuming the total score is 85

        // Simulating the request body (JSON for exam submission)
        String submissionJson = "{ \"userId\": 1, \"sessionId\": \"session123\", \"sectionAnswers\": { \"section1\": [] } }";

        // Simulating a POST request and checking response
        mockMvc.perform(post("/exam/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(submissionJson))
                .andExpect(status().isOk())
                .andExpect(content().string("85")); // Expecting total score to be 85

        // Verify interaction with the service layer
        verify(examService, times(1)).gradeExam(any(ExamSubmissionDTO.class));
    }
    @Test
    void testGetLastAnsweredExamSuccess() throws Exception {
        // Mocking the service response for fetching the last answered exam
        Exam mockExam = new Exam();
        when(examService.getSubmittedExamBySessionId("session123")).thenReturn(mockExam);

        // Simulating a GET request and verifying the response
        mockMvc.perform(get("/exam/answered")
                        .param("sessionId", "session123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist()); // Mock response will not have an ID

        // Verify interaction with the service layer
        verify(examService, times(1)).getSubmittedExamBySessionId("session123");
    }



    @Test
    void testRegisterUserSuccess() throws Exception {
        // Mocking user registration behavior
        User mockUser = new User();
        mockUser.setUsername("john_doe");
        when(userService.getUserByUsername("john_doe")).thenReturn(null);
        when(userService.registerUser("john_doe")).thenReturn(mockUser);

        // Simulating a POST request to register user
        mockMvc.perform(post("/exam/user")
                        .param("username", "john_doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe"));

        // Verify interaction with service layer
        verify(userService, times(1)).getUserByUsername("john_doe");
        verify(userService, times(1)).registerUser("john_doe");
    }
}