package com.codeless.api.automation.controller;


import static com.codeless.api.automation.util.RestApiConstant.EXECUTION_RESOURCE;
import static com.codeless.api.automation.util.RestApiConstant.RESULT_RESOURCE;

import com.codeless.api.automation.dto.ExecutionRequest;
import com.codeless.api.automation.dto.ExecutionResult;
import com.codeless.api.automation.dto.PageRequest;
import com.codeless.api.automation.service.ExecutionService;
import com.codeless.api.automation.util.DefaultValueUtil;
import java.security.Principal;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
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
      @RequestParam(name = "max_results", defaultValue = "25")
      @Min(value = 1, message="max_results is positive number, min 1 is required") Integer maxResults,
      @RequestParam(name = "next_token", required = false) @Size(max = 200) String nextToken,
      @RequestParam(name = "schedule_id", required = false) @Size(min = 40, max = 40) String scheduleId,
      Principal principal) {
    if (Objects.nonNull(scheduleId)) {
      return executionService.getExecutionsByScheduleId(
          scheduleId,
          DefaultValueUtil.getMaxResultsOrDefault(maxResults),
          nextToken,
          principal.getName());
    }
    return executionService.getAllExecutions(
        DefaultValueUtil.getMaxResultsOrDefault(maxResults),
        nextToken,
        principal.getName());
  }

  @GetMapping("/{executionId}" + RESULT_RESOURCE)
  public ExecutionResult getExecutionResult(@PathVariable String executionId, Principal principal) {
    return executionService.getExecutionResult(executionId, principal.getName());
  }
}
