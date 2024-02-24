package com.codeless.api.automation.controller;

import static com.codeless.api.automation.util.RestApiConstant.SCHEDULES_RESOURCE;

import com.codeless.api.automation.dto.PageRequest;
import com.codeless.api.automation.dto.ScheduleRequest;
import com.codeless.api.automation.dto.UpdateScheduleRequest;
import com.codeless.api.automation.service.ScheduleService;
import com.codeless.api.automation.util.DefaultValueUtil;
import java.security.Principal;
import javax.validation.Valid;
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
      @RequestParam(name = "max_results", defaultValue = "25") @Size(min = 1) Integer maxResults,
      @RequestParam(name = "next_token", required = false) @Size(max = 200) String nextToken,
      Principal principal) {
    return scheduleService.getAllSchedules(
        DefaultValueUtil.getMaxResultsOrDefault(maxResults),
        nextToken,
        principal.getName());
  }

  @DeleteMapping(path = "/{scheduleId}")
  public void deleteSchedule(@PathVariable String scheduleId, Principal principal) {
    scheduleService.deleteSchedule(scheduleId, principal.getName());
  }

}
