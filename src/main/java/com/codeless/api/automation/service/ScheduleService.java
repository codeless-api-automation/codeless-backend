package com.codeless.api.automation.service;

import com.codeless.api.automation.dto.Page;
import com.codeless.api.automation.dto.Schedule;
import java.security.Principal;

public interface ScheduleService {

  Schedule runSchedule(Schedule schedule, Principal principal);

  Page<Schedule> getSchedules(Integer page, Integer size, Principal principal);

}
