package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.appconfig.CountryConfig.RegionDetails;
import com.codeless.api.automation.appconfig.CountryConfigProvider;
import com.codeless.api.automation.converter.EmailListConverter;
import com.codeless.api.automation.converter.NextTokenConverter;
import com.codeless.api.automation.converter.TimerConverter;
import com.codeless.api.automation.dto.PageRequest;
import com.codeless.api.automation.dto.ScheduleRequest;
import com.codeless.api.automation.entity.Schedule;
import com.codeless.api.automation.entity.Test;
import com.codeless.api.automation.entity.enums.ExecutionType;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.repository.ScheduleRepository;
import com.codeless.api.automation.repository.TestRepository;
import com.codeless.api.automation.service.CronExpressionBuilderService;
import com.codeless.api.automation.service.ScheduleService;
import com.codeless.api.automation.util.ObjectBuilder;
import com.codeless.api.automation.util.RandomIdGenerator;
import com.codeless.api.automation.util.TaskLaunchArgumentsService;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

  private final SchedulerClientImpl schedulerClient;
  private final TaskLaunchArgumentsService taskLaunchArgumentsService;
  private final ScheduleRepository scheduleRepository;
  private final TestRepository testRepository;
  private final CronExpressionBuilderService cronExpressionBuilderService;
  private final TimerConverter timerConverter;
  private final EmailListConverter emailListConverter;
  private final CountryConfigProvider countryConfigProvider;
  private final NextTokenConverter nextTokenConverter;

  @Override
  public void createSchedule(ScheduleRequest scheduleRequest, String customerId) {
    Test test = testRepository.get(scheduleRequest.getTestId());
    if (Objects.isNull(test)) {
      throw new ApiException("The test was not found!", HttpStatus.BAD_REQUEST.value());
    }
    if (!test.getCustomerId().equals(customerId)) {
      throw new ApiException("Unauthorized to access!", HttpStatus.UNAUTHORIZED.value());
    }

    Map<String, RegionDetails> regionByName = countryConfigProvider.getRegions();
    if (!regionByName.containsKey(scheduleRequest.getRegion().getCity())) {
      throw new ApiException(
          String.format("Region with city name '%s' is not found.",
              scheduleRequest.getRegion().getCity()), HttpStatus.BAD_REQUEST.value());
    }
    RegionDetails regionDetails = regionByName.get(scheduleRequest.getRegion().getCity());

    Schedule scheduleEntity = new Schedule();
    scheduleEntity.setId(RandomIdGenerator.generateScheduleId());
    scheduleEntity.setCustomerId(customerId);
    scheduleEntity.setName(scheduleRequest.getScheduleName());
    scheduleEntity.setRegionName(regionDetails.getName());
    scheduleEntity.setTimer(timerConverter.toString(scheduleRequest.getTimer()));
    scheduleEntity.setEmails(emailListConverter.toString(scheduleRequest.getEmails()));
    scheduleEntity.setTestId(scheduleRequest.getTestId());
    scheduleEntity.setCreated(Instant.now());
    scheduleEntity.setLastModified(Instant.now());

    scheduleRepository.create(scheduleEntity);

    try {
      schedulerClient.createSchedule(
          regionDetails.getAwsCloudRegion(),
          scheduleEntity.getId(),
          String.format("cron(%s)",
              cronExpressionBuilderService.buildCronExpression(
                  ObjectBuilder.buildCronExpressionContext(scheduleRequest.getTimer()))),
          buildPayload(scheduleEntity));
    } catch (Exception exception) {
      log.error("Schedule was not created for {}", scheduleEntity.getId(), exception);
      scheduleRepository.delete(scheduleEntity.getId());
      throw exception;
    }
  }

  @Override
  public PageRequest<ScheduleRequest> getAllSchedules(
      Integer maxResults,
      String nextToken,
      String customerId) {

    Page<Schedule> schedules = scheduleRepository.listSchedulesByCustomerId(
        customerId,
        nextTokenConverter.fromString(nextToken),
        maxResults);

    Map<String, RegionDetails> regionByName = countryConfigProvider.getRegions();

    List<ScheduleRequest> items = schedules.items().stream()
        .map(schedule -> ScheduleRequest.builder()
            .id(schedule.getId())
            .scheduleName(schedule.getName())
            .testId(schedule.getTestId())
            .emails(emailListConverter.fromString(schedule.getEmails()))
            .timer(timerConverter.fromString(schedule.getTimer()))
            .region(ObjectBuilder.buildRegion(schedule.getRegionName(), regionByName))
            .build())
        .collect(Collectors.toList());

    return PageRequest.<ScheduleRequest>builder()
        .items(items)
        .nextToken(nextTokenConverter.toString(schedules.lastEvaluatedKey()))
        .build();
  }

  private Map<String, String> buildPayload(Schedule scheduleEntity) {
    Map<String, String> payload = new HashMap<>();
    payload.putAll(taskLaunchArgumentsService.getScheduleIdArgument(scheduleEntity.getId()));
    payload.putAll(taskLaunchArgumentsService.getExecutionTypeArgument(ExecutionType.SCHEDULED_EXECUTION.getName()));
    return payload;
  }
}
