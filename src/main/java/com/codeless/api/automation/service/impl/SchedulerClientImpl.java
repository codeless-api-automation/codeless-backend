package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.configuration.AwsConfiguration;
import com.codeless.api.automation.exception.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.scheduler.SchedulerClient;
import software.amazon.awssdk.services.scheduler.model.CreateScheduleRequest;
import software.amazon.awssdk.services.scheduler.model.FlexibleTimeWindow;
import software.amazon.awssdk.services.scheduler.model.FlexibleTimeWindowMode;
import software.amazon.awssdk.services.scheduler.model.Target;

@RequiredArgsConstructor
public class SchedulerClientImpl implements com.codeless.api.automation.service.SchedulerClient {

  private final ObjectMapper objectMapper;
  private final Map<Region, SchedulerClient> schedulerClientByRegion;
  private final AwsConfiguration awsConfiguration;

  public void createSchedule(
      String region,
      String scheduleName,
      String scheduleExpression,
      Map<String, String> payload) {

    SchedulerClient schedulerClient = schedulerClientByRegion.get(Region.of(region));
    if (schedulerClient == null) {
      throw new ApiException("Requested region is not available yet",
          HttpStatus.BAD_REQUEST.value());
    }

    String lambda = String.format("arn:aws:lambda:%s:*:function:%s",
        region,
        awsConfiguration.getFunctionName());
    Target sqsTarget = Target.builder()
        .roleArn(String.format("arn:aws:iam::*:role/%s", awsConfiguration.getScheduleRoleName()))
        .arn(lambda)
        .input(toString(payload))
        .build();
    CreateScheduleRequest createScheduleRequest = CreateScheduleRequest.builder()
        .name(scheduleName)
        .scheduleExpression(scheduleExpression)
        .target(sqsTarget)
        .flexibleTimeWindow(FlexibleTimeWindow.builder()
            .mode(FlexibleTimeWindowMode.OFF)
            .build())
        .build();

    schedulerClient.createSchedule(createScheduleRequest);
  }

  private String toString(Map<String, String> payload) {
    try {
      return objectMapper.writeValueAsString(payload);
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

}
