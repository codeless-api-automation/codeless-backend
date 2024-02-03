package com.codeless.api.automation.service;

import com.codeless.api.automation.dto.PageRequest;
import com.codeless.api.automation.dto.ScheduleRequest;
import com.codeless.api.automation.dto.UpdateScheduleRequest;

public interface ScheduleService {

  void createSchedule(ScheduleRequest scheduleRequest, String customerId);

  PageRequest<ScheduleRequest> getAllSchedules(
      Integer maxResults,
      String nextToken,
      String customerId);

  void deleteSchedule(String scheduleId, String customerId);

  void updateSchedule(UpdateScheduleRequest updateScheduleRequest, String customerId);
}
