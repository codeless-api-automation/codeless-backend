package com.codeless.api.automation.controller;

import static com.codeless.api.automation.util.RestApiConstant.SCHEDULES_RESOURCE;

import com.codeless.api.automation.dto.Schedule;
import com.codeless.api.automation.service.ScheduleService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(SCHEDULES_RESOURCE)
@Validated
@RequiredArgsConstructor
public class ScheduleController {

  private final ScheduleService scheduleService;

  @PostMapping
  public Schedule createSchedule(@RequestBody @Valid Schedule schedule) {
    return scheduleService.runSchedule(schedule);
  }

}
