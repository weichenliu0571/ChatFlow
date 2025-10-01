package com.chatflow.chatflow.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiErrors {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> badRequest(IllegalArgumentException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<String> conflict(IllegalStateException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<String> constraint(DataIntegrityViolationException e) {
    String msg = (e.getMostSpecificCause() != null)
        ? e.getMostSpecificCause().getMessage()
        : e.getMessage();
    return ResponseEntity.status(HttpStatus.CONFLICT).body("Constraint violation: " + msg);
  }

  // 👇 fallback for ANY other error (NullPointerException, etc.)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> server(Exception e) {
    log.error("Unhandled error", e); // full stack trace in console
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(e.getClass().getSimpleName() + ": " + (e.getMessage() == null ? "" : e.getMessage()));
  }
}
