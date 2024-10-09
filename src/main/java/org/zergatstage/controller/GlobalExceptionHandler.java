package org.zergatstage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zergatstage.DTO.ResponseDTO;

import java.io.IOException;

/**
 * @author father
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ResponseDTO> handleIllegalArgument(IllegalArgumentException ex) {

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.builder()
            .errorMessage(ex.getMessage())
            .businessMessage(ex.getMessage())
            .build());
  }

  @ExceptionHandler(IOException.class)
  public ResponseEntity<String> handleIOException(IOException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("File processing error: " + ex.getMessage());
  }
}
