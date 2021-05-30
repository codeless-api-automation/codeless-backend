package com.codeless.api.automation.controller;

import com.codeless.api.automation.dto.ApiError;
import com.codeless.api.automation.exception.ApiException;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionTranslator {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ApiError> handleApiException(ApiException apiException) {
    return ResponseEntity.status(apiException.getStatusCode())
        .body(buildApiError(HttpStatus.valueOf(apiException.getStatusCode()),
            apiException.getMessage()));
  }

  private ApiError buildApiError(HttpStatus httpStatus, String message) {
    ApiError apiError = new ApiError();
    apiError.setTimestamp(new Date().getTime());
    apiError.setError(httpStatus.getReasonPhrase());
    apiError.setStatus(httpStatus.value());
    apiError.setMessage(message);
    return apiError;
  }
}
