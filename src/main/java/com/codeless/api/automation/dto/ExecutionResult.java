package com.codeless.api.automation.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = ExecutionResult.ExecutionResultBuilder.class)
public class ExecutionResult {

  ExecutionRequest execution;
  Result result;
}
