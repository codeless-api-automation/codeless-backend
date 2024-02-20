package com.codeless.api.automation.service;

import com.codeless.api.automation.dto.ExecutionRequest;
import com.codeless.api.automation.dto.ExecutionResult;
import com.codeless.api.automation.dto.PageRequest;

public interface ExecutionService {

  ExecutionRequest createExecution(ExecutionRequest execution, String customerId);
  PageRequest<ExecutionRequest> getAllExecutions(
      Integer maxResults,
      String nextToken,
      String customerId);
  PageRequest<ExecutionRequest> getExecutionsByScheduleId(
      String scheduleId,
      Integer maxResults,
      String nextToken,
      String customerId);
  ExecutionResult getExecutionResult(String executionId, String customerId);
}
