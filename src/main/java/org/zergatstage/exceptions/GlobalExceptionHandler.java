package org.zergatstage.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zergatstage.DTO.ResponseDTO;

import java.io.IOException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Helper method to determine if the request expects JSON
    private boolean isJsonRequest(HttpServletRequest request) {
        String acceptHeader = request.getHeader("Accept");
        String contentType = request.getHeader("Content-Type");

        return (acceptHeader != null && acceptHeader.contains("application/json")) ||
                (contentType != null && contentType.contains("application/json"));
    }

    @ExceptionHandler(JsonProcessingException.class)
    public Object handleJsonProcessingException(JsonProcessingException ex, HttpServletRequest request) {
        log.error(ex.getMessage());
        if (isJsonRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse("JSON processing error: " + ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return "error"; // Returns error.html for web requests
    }

    @ExceptionHandler(UnrecognizedPropertyException.class)
    public Object handleUnrecognizedPropertyException(UnrecognizedPropertyException ex, HttpServletRequest request) {
        if (isJsonRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse("Unknown field in processed JSON: " + ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return "error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgument(IllegalArgumentException ex,
                                        HttpServletRequest request,
                                        Model model) {
        log.error(ex.getMessage());
        if (isJsonRequest(request)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(
                            ex.getMessage(), null));
        }
        // For web requests, add message to model
        model.addAttribute("businessMessage: ", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(IOException.class)
    public Object handleIOException(IOException ex, HttpServletRequest request) {
        log.error(ex.getMessage());
        if (isJsonRequest(request)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File processing error: " + ex.getMessage());
        }
        return "error";
    }

    /**
     * Handles all uncaught exceptions not explicitly handled by other methods.
     *
     * @param ex      The exception.
     * @param request The incoming HTTP request.
     * @param model   The UI model used for web requests.
     * @return A JSON or HTML error response depending on the request type.
     */
    @ExceptionHandler(Exception.class)
    public Object handleAllOtherExceptions(Exception ex, HttpServletRequest request, Model model) {
        String message = "Internal server error: " + ex.getMessage();
        log.error(ex.getMessage());
        if (isJsonRequest(request)) {

            ErrorResponse errorResponse = new ErrorResponse(message);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        model.addAttribute("errorMessage", message);  // For Thymeleaf or JSP to show the actual error
        return "error";
    }
}