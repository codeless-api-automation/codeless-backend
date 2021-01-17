package com.codeless.api.automation.controller;

import com.codeless.api.automation.exception.ApiException;
import javax.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionTranslator extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<String> handleApiException(ApiException apiException) {
    return ResponseEntity.status(apiException.getStatusCode()).body(apiException.getMessage());
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<String> handleValidationException(ValidationException validationException) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationException.getMessage());
  }
}
