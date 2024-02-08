package com.codeless.api.automation.service.impl;

import static com.codeless.api.automation.util.RestApiConstant.UNAUTHORIZED_MESSAGE;

import com.codeless.api.automation.appconfig.CountryConfig.RegionDetails;
import com.codeless.api.automation.appconfig.CountryConfigProvider;
import com.codeless.api.automation.converter.EmailListConverter;
import com.codeless.api.automation.converter.NextTokenConverter;
import com.codeless.api.automation.converter.TimerConverter;
import com.codeless.api.automation.dto.NextToken;
import com.codeless.api.automation.dto.PageRequest;
import com.codeless.api.automation.dto.ScheduleRequest;
import com.codeless.api.automation.dto.UpdateScheduleRequest;
import com.codeless.api.automation.entity.Schedule;
import com.codeless.api.automation.entity.Test;
import com.codeless.api.automation.entity.enums.ExecutionType;
import com.codeless.api.automation.entity.enums.ScheduleState;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.repository.ScheduleRepository;
import com.codeless.api.automation.repository.TestRepository;
import com.codeless.api.automation.service.CronExpressionBuilderService;
import com.codeless.api.automation.service.ScheduleService;
import com.codeless.api.automation.util.ApiValidationUtil;
import com.codeless.api.automation.util.ObjectBuilder;
import com.codeless.api.automation.util.PersistenceUtil;
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
      throw new ApiException(UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED.value());
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
    scheduleEntity.setScheduleState(ScheduleState.ENABLED);
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
      String nextTokenAsString,
      String customerId) {
    NextToken nextToken = nextTokenConverter.fromString(nextTokenAsString);
    ApiValidationUtil.validateNextTokenForRequestByCustomerId(nextToken);
    ApiValidationUtil.validateNextTokenOwnership(nextToken, customerId);

    Page<Schedule> schedules = scheduleRepository.listSchedulesByCustomerId(
        customerId,
        PersistenceUtil.buildLastEvaluatedKeyForRequestByCustomerId(nextToken),
        maxResults);

    Map<String, RegionDetails> regionByName = countryConfigProvider.getRegions();

    List<ScheduleRequest> items = schedules.items().stream()
        .map(schedule -> ScheduleRequest.builder()
            .id(schedule.getId())
            .state(schedule.getScheduleState())
            .scheduleName(schedule.getName())
            .testId(schedule.getTestId())
            .timer(timerConverter.fromString(schedule.getTimer()))
            .region(ObjectBuilder.buildRegion(schedule.getRegionName(), regionByName))
            .build())
        .collect(Collectors.toList());
    return PageRequest.<ScheduleRequest>builder()
        .items(items)
        .nextToken(nextTokenConverter.toString(
            PersistenceUtil.buildNextTokenForRequestByCustomerId(schedules.lastEvaluatedKey())))
        .build();
  }

  @Override
  public void deleteSchedule(String scheduleId, String customerId) {
    Schedule schedule = scheduleRepository.get(scheduleId);
    if (Objects.isNull(schedule)) {
      throw new ApiException("The schedule was not found!", HttpStatus.BAD_REQUEST.value());
    }
    if (!schedule.getCustomerId().equals(customerId)) {
      throw new ApiException(UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED.value());
    }
    schedulerClient.deleteSchedule(schedule.getRegionName(), schedule.getId());
    scheduleRepository.delete(scheduleId);
  }

  @Override
  public void updateSchedule(UpdateScheduleRequest updateScheduleRequest, String customerId) {
    if (Objects.isNull(updateScheduleRequest.getState())
        && Objects.isNull(updateScheduleRequest.getEmails())) {
      throw new ApiException("Request to update schedule is missing state or emails!",
          HttpStatus.BAD_REQUEST.value());
    }
    Schedule schedule = scheduleRepository.get(updateScheduleRequest.getId());
    if (Objects.isNull(schedule)) {
      throw new ApiException("The schedule was not found!", HttpStatus.BAD_REQUEST.value());
    }
    if (!schedule.getCustomerId().equals(customerId)) {
      throw new ApiException(UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED.value());
    }

    String existingEmails = schedule.getEmails();
    String newEmails = emailListConverter.toString(updateScheduleRequest.getEmails());
    if (Objects.nonNull(newEmails) && !newEmails.equals(existingEmails)) {
      schedule.setEmails(newEmails);
    }
    ScheduleState existingScheduleState = schedule.getScheduleState();
    ScheduleState newScheduleState = updateScheduleRequest.getState();
    if (Objects.nonNull(newScheduleState) && !existingScheduleState.equals(newScheduleState)) {
      schedule.setScheduleState(newScheduleState);
    }
    // TODO: update DDB
    //scheduleRepository.put();

    // if old state is not equal to new state, then update schedule
    try {
      schedulerClient.updateSchedule(schedule.getRegionName(), schedule.getId(), null);
    } catch (Exception exception) {
      schedule.setEmails(existingEmails);
      schedule.setScheduleState(existingScheduleState);
      //scheduleRepository.put();
      throw exception;
    }
  }

  private Map<String, String> buildPayload(Schedule scheduleEntity) {
    Map<String, String> payload = new HashMap<>();
    payload.putAll(taskLaunchArgumentsService.getScheduleIdArgument(scheduleEntity.getId()));
    payload.putAll(taskLaunchArgumentsService
        .getExecutionTypeArgument(ExecutionType.SCHEDULED_EXECUTION.getName()));
    return payload;
  }
}
