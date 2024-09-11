package org.zergatstage.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zergatstage.model.Exam;
import org.zergatstage.model.Submissions;
import org.zergatstage.model.User;
import org.zergatstage.services.ExamService;
import org.zergatstage.services.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author father
 */

@Controller
public class SimpleWebController {


    private final ExamService examService;
    private final UserService userService;
    private final HttpSession session;

    public SimpleWebController(ExamService examService, UserService userService, HttpSession session) {
        this.examService = examService;
        this.userService = userService;
        this.session = session;
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


    @GetMapping("/quiz")
    public String quiz( Model m, RedirectAttributes ra) {
        String username = (String) session.getAttribute("username");
        String x = checkUser(username, ra);
        if (x != null) return x; //redirect
        User user = userService.getUserByUsername(username);
        Exam qForm = examService.getExam(user, 2, 2);// TODO: replace fetch from model
        m.addAttribute("qForm", qForm);
        return "quiz";
    }

    private static String checkUser(String username, RedirectAttributes ra) {
        if (username == null || username.isEmpty()) {
            ra.addFlashAttribute("warning", "You must enter your name");
            return "redirect:/";
        }
        return null;
    }

    @PostMapping("/submitQuiz")
    public ResponseEntity<String> submit(@RequestBody Exam exam, Model m) {
        //TODO: recalculate of results
        //save the exam result
        examService.saveSubmittedExam(exam);

        return ResponseEntity.ok("Submitted");
    }



    @GetMapping("/submission")
    public String getSubmission(@RequestParam("id") String submissionId, Model model){
        Exam submission = examService.getSubmittedExamBySessionId(submissionId);
        model.addAttribute("qForm", submission);
        return "/quiz-review";
    }

    @GetMapping("/result")
    public String redirectLazyToResult(Model model) {
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("totalScore", 97); //Just for fun. Why not?
        model.addAttribute("totalCorrect" , 100);
        return "result.html";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteSubmission(@RequestParam("submissionId") String submissionId, RedirectAttributes redirectAttributes) {
        try {
            Exam exam = examService.getSubmittedExamBySessionId(submissionId);
           examService.deleteSubmission(exam);
            // Add a success message after deletion
            redirectAttributes.addFlashAttribute("successMessage", "Submission deleted successfully.");

        } catch (Exception e) {
            // Handle the case where the submission might not exist or deletion fails
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete submission. Submission not found or another issue occurred.");
        }
        // Redirect to the submissions page after deletion
        return "redirect:/submissions"; // Assumed to be the endpoint for the submissions page
    }

    //some helping endpoint to use prism js library and check hypnotises
    @GetMapping("/code")
    public String getCode() {
        return "templates-code.html";
    }
}
