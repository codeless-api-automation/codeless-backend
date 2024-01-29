package com.codeless.api.automation.controller;


import static com.codeless.api.automation.util.RestApiConstant.EXECUTION_RESOURCE;
import static com.codeless.api.automation.util.RestApiConstant.RESULT_RESOURCE;

import com.codeless.api.automation.dto.ExecutionRequest;
import com.codeless.api.automation.dto.ExecutionResult;
import com.codeless.api.automation.dto.PageRequest;
import com.codeless.api.automation.service.ExecutionService;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(EXECUTION_RESOURCE)
@Validated
@RequiredArgsConstructor
public class ExecutionController {

  private final ExecutionService executionService;

  @PostMapping
  public ExecutionRequest createExecution(@RequestBody @Valid ExecutionRequest execution,
      Principal principal) {
    return executionService.createExecution(execution, principal.getName());
  }

  @GetMapping
  public PageRequest<ExecutionRequest> getAllExecutions(
      @RequestParam(defaultValue = "25") Integer maxResults, String nextToken, Principal principal) {
    return executionService.getAllExecutions(maxResults, nextToken, principal.getName());
  }

  @GetMapping("/{executionId}" + RESULT_RESOURCE)
  public ExecutionResult getExecutionResult(@PathVariable String executionId, Principal principal) {
    return executionService.getExecutionResult(executionId, principal.getName());
  }
}
