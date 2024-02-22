package com.codeless.api.automation.service.impl;

import static com.codeless.api.automation.util.RestApiConstant.SUPPORT_TICKET_MESSAGE;

import com.codeless.api.automation.configuration.AwsConfiguration;
import com.codeless.api.automation.exception.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.scheduler.SchedulerClient;
import software.amazon.awssdk.services.scheduler.model.CreateScheduleRequest;
import software.amazon.awssdk.services.scheduler.model.DeleteScheduleRequest;
import software.amazon.awssdk.services.scheduler.model.FlexibleTimeWindow;
import software.amazon.awssdk.services.scheduler.model.FlexibleTimeWindowMode;
import software.amazon.awssdk.services.scheduler.model.ScheduleState;
import software.amazon.awssdk.services.scheduler.model.Target;
import software.amazon.awssdk.services.scheduler.model.UpdateScheduleRequest;

@Component
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

    String lambda = String.format("arn:aws:lambda:%s:%s:function:%s",
        region,
        awsConfiguration.getScheduleAccountId(),
        awsConfiguration.getFunctionName());
    Target sqsTarget = Target.builder()
        .roleArn(String.format("arn:aws:iam::%s:role/%s",
            awsConfiguration.getScheduleAccountId(),
            awsConfiguration.getScheduleRoleName()))
        .arn(lambda)
        .input(toString(payload))
        .build();
    CreateScheduleRequest createScheduleRequest = CreateScheduleRequest.builder()
        .name(scheduleName)
        .scheduleExpression(scheduleExpression)
        .target(sqsTarget)
        .flexibleTimeWindow(FlexibleTimeWindow.builder()
            .maximumWindowInMinutes(1)
            .mode(FlexibleTimeWindowMode.FLEXIBLE)
            .build())
        .build();

    schedulerClient.createSchedule(createScheduleRequest);
  }

  @Override
  public void deleteSchedule(String region, String scheduleName) {
    SchedulerClient schedulerClient = schedulerClientByRegion.get(Region.of(region));
    if (schedulerClient == null) {
      throw new ApiException(SUPPORT_TICKET_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
    DeleteScheduleRequest deleteScheduleRequest = DeleteScheduleRequest.builder()
        .name(scheduleName)
        .build();
    schedulerClient.deleteSchedule(deleteScheduleRequest);
  }

  public void updateSchedule(
      String region,
      String scheduleName,
      boolean isEnabled) {
    SchedulerClient schedulerClient = schedulerClientByRegion.get(Region.of(region));
    if (schedulerClient == null) {
      throw new ApiException("Requested region is not available yet",
          HttpStatus.BAD_REQUEST.value());
    }
    UpdateScheduleRequest updateScheduleRequest = UpdateScheduleRequest.builder()
        .state(isEnabled ? ScheduleState.ENABLED : ScheduleState.DISABLED)
        .name(scheduleName)
        .build();
    schedulerClient.updateSchedule(updateScheduleRequest);
  }

  private String toString(Map<String, String> payload) {
    try {
      return objectMapper.writeValueAsString(payload);
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

}
