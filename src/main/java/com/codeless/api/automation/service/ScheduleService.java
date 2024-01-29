package com.codeless.api.automation.service;

import com.codeless.api.automation.dto.PageRequest;
import com.codeless.api.automation.dto.ScheduleRequest;

public interface ScheduleService {

  void createSchedule(ScheduleRequest scheduleRequest, String customerId);

  PageRequest<ScheduleRequest> getAllSchedules(
      Integer maxResults,
      String nextToken,
      String customerId);

}
