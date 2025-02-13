package org.zergatstage.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error-404")
    public String handleNotFound(HttpServletRequest request) {
        // Log the requested URL that caused 404
        String requestedUrl = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        return "404";  // This will render your 404.html template
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // For explicit 404 requests, redirect to 404 handler
        if (status != null && Integer.parseInt(status.toString()) == HttpStatus.NOT_FOUND.value()) {
            return "redirect:/error-404";
        }

        // Handle other errors as before
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        if (exception instanceof Exception) {
            Exception ex = (Exception) exception;
            if (ex instanceof IllegalArgumentException) {
                model.addAttribute("businessMessage", ex.getMessage());
            } else {
                model.addAttribute("errorMessage", ex.getMessage());
            }
        }

        return "error";
    }
}