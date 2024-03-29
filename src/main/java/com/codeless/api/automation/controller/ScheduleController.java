package com.codeless.api.automation.controller;

import static com.codeless.api.automation.util.RestApiConstant.SCHEDULES_RESOURCE;

import com.codeless.api.automation.dto.ExecutionRequest;
import com.codeless.api.automation.dto.PageRequest;
import com.codeless.api.automation.dto.ScheduleRequest;
import com.codeless.api.automation.dto.UpdateScheduleRequest;
import com.codeless.api.automation.service.ExecutionService;
import com.codeless.api.automation.service.ScheduleService;
import com.codeless.api.automation.util.DefaultValueUtil;
import java.security.Principal;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(SCHEDULES_RESOURCE)
@Validated
@RequiredArgsConstructor
public class ScheduleController {

  private final ScheduleService scheduleService;
  private final ExecutionService executionService;

  @PutMapping
  public ResponseEntity<Void> updateSchedule(
      @RequestBody @Valid UpdateScheduleRequest updateScheduleRequest,
      Principal principal) {
    scheduleService.updateSchedule(updateScheduleRequest, principal.getName());
    return ResponseEntity
        .noContent()
        .build();
  }

  @PostMapping
  public void createSchedule(
      @RequestBody @Valid ScheduleRequest scheduleRequest,
      Principal principal) {
    scheduleService.createSchedule(scheduleRequest, principal.getName());
  }

  @GetMapping
  public PageRequest<ScheduleRequest> getAllSchedules(
      @RequestParam(name = "max_results", defaultValue = "25")
      @Min(value = 1, message = "max_results is positive number, min 1 is required") Integer maxResults,
      @RequestParam(name = "next_token", required = false) @Size(max = 200) String nextToken,
      Principal principal) {
    return scheduleService.getAllSchedules(
        DefaultValueUtil.getMaxResultsOrDefault(maxResults),
        nextToken,
        principal.getName());
  }

  @GetMapping(path = "/{scheduleId}/executions")
  public PageRequest<ExecutionRequest> getAllExecutionsByScheduleId(
      @PathVariable @Size(min = 40, max = 40) String scheduleId,
      @RequestParam(name = "max_results", defaultValue = "25")
      @Min(value = 1, message = "max_results is positive number, min 1 is required") Integer maxResults,
      @RequestParam(name = "next_token", required = false) @Size(max = 200) String nextToken,
      Principal principal) {
    return executionService.getExecutionsByScheduleId(
        scheduleId,
        DefaultValueUtil.getMaxResultsOrDefault(maxResults),
        nextToken,
        principal.getName());
  }

  @DeleteMapping(path = "/{scheduleId}")
  public void deleteSchedule(@PathVariable @Size(min = 40, max = 40) String scheduleId,
      Principal principal) {
    scheduleService.deleteSchedule(scheduleId, principal.getName());
  }

}
