package com.codeless.api.automation.service;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CleanupJobService {

  @Value("${codeless.job.delete-expired-executions.interval-in-cron.expiration-time}")
  private String expirationTime;

  @Scheduled(cron = "${codeless.job.delete-expired-executions.interval-in-cron}")
  @SchedulerLock(name = "delete-expired-executions")
  public void deleteExpiredExecutions() {
    // TODO: delete old executions and their results
  }

}
