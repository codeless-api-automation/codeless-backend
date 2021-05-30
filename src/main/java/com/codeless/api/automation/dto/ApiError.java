package com.codeless.api.automation.dto;

import lombok.Data;

@Data
public class ApiError {

  private Long timestamp;
  private int status;
  private String error;
  private String message;
}
