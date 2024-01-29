package com.codeless.api.automation.controller;

import static com.codeless.api.automation.util.RestApiConstant.SCHEDULES_RESOURCE;

import com.codeless.api.automation.dto.PageRequest;
import com.codeless.api.automation.dto.ScheduleRequest;
import com.codeless.api.automation.service.ScheduleService;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

  @PostMapping
  public void createSchedule(
      @RequestBody @Valid ScheduleRequest scheduleRequest,
      Principal principal) {
    scheduleService.createSchedule(scheduleRequest, principal.getName());
  }

  @GetMapping
  public PageRequest<ScheduleRequest> getAllSchedules(
      @RequestParam(defaultValue = "25") Integer maxResults,
      String nextToken, Principal principal) {
    return scheduleService.getAllSchedules(maxResults, nextToken, principal.getName());
  }

}
