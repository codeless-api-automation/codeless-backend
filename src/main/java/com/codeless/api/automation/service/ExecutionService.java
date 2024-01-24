package com.codeless.api.automation.service;

import com.codeless.api.automation.dto.Execution;
import com.codeless.api.automation.dto.ExecutionResult;
import com.codeless.api.automation.dto.Page;
import java.security.Principal;

public interface ExecutionService {

  Execution runExecution(Execution execution, Principal principal);
  Page<Execution> getExecutions(Integer page, Integer size, Principal principal);
  ExecutionResult getExecutionResult(long executionId, Principal principal);
}
