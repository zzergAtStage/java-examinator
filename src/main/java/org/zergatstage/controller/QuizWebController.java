package org.zergatstage.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zergatstage.model.Exam;
import org.zergatstage.model.JavaQuizQuestion;
import org.zergatstage.model.SubmissionResult;
import org.zergatstage.model.Submissions;
import org.zergatstage.model.User;
import org.zergatstage.repository.JavaQuizRepository;
import org.zergatstage.services.ExamService;
import org.zergatstage.services.UserService;
import org.zergatstage.services.validation.QuestionValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author father
 */

@Controller
public class QuizWebController {


  private final ExamService examService;
  private final UserService userService;
  private final HttpSession session;
  private final JavaQuizRepository javaQuizRepository;
  private final QuestionValidator questionValidator;

  public QuizWebController(ExamService examService, UserService userService, HttpSession session, JavaQuizRepository repository, QuestionValidator questionValidator) {
    this.examService = examService;
    this.userService = userService;
    this.session = session;
    this.javaQuizRepository = repository;
      this.questionValidator = questionValidator;
  }

  private static String checkUser(String username, RedirectAttributes ra) {
    if (username == null || username.isEmpty()) {
      ra.addFlashAttribute("warning", "You must enter your name");
      return "redirect:/";
    }
    return null;
  }

  @GetMapping("/")
  public String home() {
    return "index.html";
  }

  @PostMapping("/register")
  public String registerUser(@RequestParam String username, Model m, RedirectAttributes ra) {
    m.addAttribute("username", username);
    session.setAttribute("username", username);
    assert username != null;
    String x = checkUser(username, ra);
    if (x != null) return x; //redirect
    User user = userService.getUserByUsername(username);
    if (user == null) {
      userService.registerUser(username);
    }
    return "redirect:/submissions";
  }

  @GetMapping("/submissions")
  public String getSubmissions(Model m, HttpSession session, RedirectAttributes redirectAttributes) {
    List<Submissions> submissions = new ArrayList<>();
    String username = (String) session.getAttribute("username");
    String x = checkUser(username, redirectAttributes);
    if (x != null) return x;
    User user = userService.getUserByUsername(username);
    m.addAttribute("username", username);
    List<Exam> exams = examService.getSubmittedExamsByUser(user);
    for (Exam exam1 : exams) {
      submissions.add(Submissions.builder()
              .id(exam1.getExam_id().intValue())
              .date(exam1.getExamDate())
              .sessionIdToLink(exam1.getSessionId())

              .build());
    }
    m.addAttribute("submissions", submissions);
    return "submissions.html";
  }

  @GetMapping("/start-quiz")
  public String startQuiz(
      @RequestParam(name="difficulty", defaultValue="easy") String difficulty,
      @RequestParam(name="count", defaultValue="10") int count,
      Model m,
      RedirectAttributes ra) {
    String username = (String) session.getAttribute("username");
    String x = checkUser(username, ra);
    int difficultyInt = 1;
    if (x != null) return x; //redirect
    User user = userService.getUserByUsername(username);
    // TODO: incorporate 'difficulty' parameter
    switch (difficulty) {
      case "easy" -> difficultyInt = 1;
      case "middle" -> difficultyInt = 2;
      case "hard" -> difficultyInt =3;
    }
    Exam qForm = examService.getExam(user, difficultyInt, count);
    m.addAttribute("qForm", qForm);
    return "quiz";
  }

  @PostMapping("/start-already-taken")
  public String startAlreadyTakenExam(@RequestParam("uuid") String uuid, Model m, RedirectAttributes ra){
    String username = (String) session.getAttribute("username");
    String x = checkUser(username, ra);
    if (x != null) return x; //redirect
    User user = userService.getUserByUsername(username);
    Exam qForm = examService.prepareExamLikeTaken(uuid, user.getId());
    m.addAttribute("qForm", qForm);
    return "quiz";
  }

  @PostMapping("/submitQuiz")
  public ResponseEntity<?> submit(@RequestBody Exam exam) {
    SubmissionResult result = examService.gradeAndSaveExam(exam);
    return ResponseEntity.ok(result);
  }

    @GetMapping("/submission")
    public String getSubmission(@RequestParam("id") String submissionId, Model model){
        Exam submission = examService.getSubmittedExamBySessionId(submissionId);
        model.addAttribute("qForm", submission);
        return "/submission";
    }

  @GetMapping("/result")
  public String redirectLazyToResult(Model model) {
    return "result";
  }

  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public String deleteSubmission(@RequestParam("submissionId") String submissionId, RedirectAttributes redirectAttributes) {
    try {
      Exam exam = examService.getSubmittedExamBySessionId(submissionId);
      examService.deleteSubmission(exam);
      redirectAttributes.addFlashAttribute("successMessage", "Submission deleted successfully.");

    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete submission. Submission not found or another issue occurred.");
    }
    return "redirect:/submissions";
  }

  //some helping endpoint to use prism js library and check hypotheses
  @GetMapping("/add_question")
  public String addQuestion(@RequestBody JavaQuizQuestion question, Model model) {
    //TODO: rework with service to bring the checks
    if (!javaQuizRepository.findByQuestionHeader(question.getQuestionHeader()).isEmpty()) {
      throw new IllegalArgumentException("Question with header: " + question.getQuestionHeader() +
              " already exists");
    }
    questionValidator.validate(question);
    javaQuizRepository.save(question);
    return "questions";
  }
}
