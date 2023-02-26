package com.codeless.api.automation.service;

import java.util.Map;

public interface SchedulerClient {

  void createSchedule(
      String region,
      String scheduleName,
      String scheduleExpression,
      Map<String, String> payload);
}
