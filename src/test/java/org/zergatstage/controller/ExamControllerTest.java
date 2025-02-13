package org.zergatstage.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.zergatstage.DTO.ExamSubmissionDTO;
import org.zergatstage.model.Exam;
import org.zergatstage.model.JavaQuizQuestion;
import org.zergatstage.model.User;
import org.zergatstage.services.ExamService;
import org.zergatstage.services.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

class ExamControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExamService examService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ExamController examController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(examController).build();
    }

    @Test
    void generateQuiz_ShouldReturnExam() throws Exception {
        when(examService.getExam(anyInt(), anyInt())).thenReturn(new Exam());

        mockMvc.perform(get("/api/v1/exam/generate")
                        .param("difficulty", "2")
                        .param("numberQuestions", "5"))
                .andExpect(status().isOk());
    }

    @Test
    void getLastAnsweredExam_ShouldReturnExam() throws Exception {
        when(examService.getSubmittedExamBySessionId(anyString())).thenReturn(new Exam());

        mockMvc.perform(get("/api/v1/exam/answered")
                        .param("sessionId", "12345"))
                .andExpect(status().isOk());
    }

    @Test
    void submitExam_ShouldReturnScore() throws Exception {
        ExamSubmissionDTO submission = new ExamSubmissionDTO();
        when(examService.gradeExam(new ExamSubmissionDTO())).thenReturn(85);

        mockMvc.perform(post("/api/v1/exam/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(submission)))
                .andExpect(status().isOk())
                .andExpect(content().string("85"));
    }

    @Test
    void registerUser_ShouldReturnUser() throws Exception {
        User user = new User();
        when(userService.getUserByUsername(anyString())).thenReturn(null);
        when(userService.registerUser(anyString())).thenReturn(user);

        mockMvc.perform(post("/api/v1/exam/user")
                        .param("username", "testUser"))
                .andExpect(status().isOk());
    }

    @Test
    void addQuestion_ShouldReturnSuccessResponse() throws Exception {
        JavaQuizQuestion question = new JavaQuizQuestion();
        doNothing().when(examService).saveUniqueQuestion(any());
                //.saveUniqueQuestion(any())).thenReturn(question);

        mockMvc.perform(post("/api/v1/exam/question")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isOk());
    }

    @Test
    void getOneQuestionById_ShouldReturnQuestion() throws Exception {
        JavaQuizQuestion question = new JavaQuizQuestion();
        when(examService.getQuestionById(anyLong())).thenReturn(question);

        mockMvc.perform(get("/api/v1/exam/question/1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateQuestion_ShouldReturnUpdatedResponse() throws Exception {
        JavaQuizQuestion question = new JavaQuizQuestion();
        doNothing().when(examService).updateQuestion(anyLong(), any());

        mockMvc.perform(put("/api/v1/exam/question/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isOk());
    }
}
