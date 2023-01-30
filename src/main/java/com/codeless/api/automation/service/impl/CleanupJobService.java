package com.codeless.api.automation.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CleanupJobService {

  @Scheduled(cron = "${codeless.job.delete-expired-executions.interval-in-cron}")
  @SchedulerLock(name = "delete-expired-executions")
  public void deleteExpiredExecutions() {
  }

}
