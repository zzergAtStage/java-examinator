package org.zergatstage.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zergatstage.DTO.ResponseDTO;

import java.io.IOException;

@ControllerAdvice
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
        if (isJsonRequest(request)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.builder()
                            .errorMessage(ex.getMessage())
                            .businessMessage(ex.getMessage())
                            .build());
        }
        // For web requests, add message to model
        model.addAttribute("businessMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(IOException.class)
    public Object handleIOException(IOException ex, HttpServletRequest request) {
        if (isJsonRequest(request)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File processing error: " + ex.getMessage());
        }
        return "error";
    }

    // Catch-all handler for unhandled exceptions
    @ExceptionHandler(Exception.class)
    public Object handleAllOtherExceptions(Exception ex, HttpServletRequest request) {
        if (isJsonRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse("Internal server error: " + ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return "error";
    }
}