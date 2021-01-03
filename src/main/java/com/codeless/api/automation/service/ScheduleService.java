package com.codeless.api.automation.service;

import com.codeless.api.automation.dto.Page;
import com.codeless.api.automation.dto.Schedule;

public interface ScheduleService {

  Schedule runSchedule(Schedule schedule);

  Page<Schedule> getSchedules(Integer page, Integer size);

}
