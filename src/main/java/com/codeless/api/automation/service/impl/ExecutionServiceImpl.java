package com.codeless.api.automation.service.impl;

import static com.codeless.api.automation.util.RestApiConstant.UNAUTHORIZED_MESSAGE;

import com.codeless.api.automation.appconfig.CountryConfig.RegionDetails;
import com.codeless.api.automation.appconfig.CountryConfigProvider;
import com.codeless.api.automation.converter.LogsConverter;
import com.codeless.api.automation.converter.NextTokenConverter;
import com.codeless.api.automation.dto.ExecutionRequest;
import com.codeless.api.automation.dto.ExecutionResult;
import com.codeless.api.automation.dto.NextToken;
import com.codeless.api.automation.dto.PageRequest;
import com.codeless.api.automation.dto.Result;
import com.codeless.api.automation.entity.Execution;
import com.codeless.api.automation.entity.Schedule;
import com.codeless.api.automation.entity.enums.ExecutionStatus;
import com.codeless.api.automation.entity.enums.ExecutionType;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.repository.ExecutionRepository;
import com.codeless.api.automation.repository.ScheduleRepository;
import com.codeless.api.automation.repository.TestRepository;
import com.codeless.api.automation.service.ExecutionClient;
import com.codeless.api.automation.service.ExecutionService;
import com.codeless.api.automation.util.ApiValidationUtil;
import com.codeless.api.automation.util.ExecutionUtil;
import com.codeless.api.automation.util.ObjectBuilder;
import com.codeless.api.automation.util.PersistenceUtil;
import com.codeless.api.automation.util.RandomIdGenerator;
import com.codeless.api.automation.util.RestApiConstant;
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
public class ExecutionServiceImpl implements ExecutionService {

  private final ExecutionClient executionClient;
  private final TaskLaunchArgumentsService taskLaunchArgumentsService;
  private final ExecutionRepository executionRepository;
  private final ScheduleRepository scheduleRepository;
  private final LogsConverter logsConverter;
  private final NextTokenConverter nextTokenConverter;
  private final TestRepository testRepository;
  private final CountryConfigProvider countryConfigProvider;

  @Override
  public ExecutionRequest createExecution(ExecutionRequest executionRequest, String customerId) {
    com.codeless.api.automation.entity.Test test = testRepository.get(executionRequest.getTestId());
    if (test == null) {
      throw new ApiException("Test is not found", HttpStatus.BAD_REQUEST.value());
    }
    if (!test.getCustomerId().equals(customerId)) {
      throw new ApiException("Unauthorized to access!", HttpStatus.UNAUTHORIZED.value());
    }

    Map<String, RegionDetails> regionByName = countryConfigProvider.getRegions();
    if (!regionByName.containsKey(executionRequest.getRegion().getCity())) {
      throw new ApiException(
          String.format("Region with city name '%s' is not found.",
              executionRequest.getRegion().getCity()), HttpStatus.BAD_REQUEST.value());
    }
    RegionDetails regionDetails = regionByName.get(executionRequest.getRegion().getCity());

    Execution execution = new Execution();
    execution.setId(RandomIdGenerator.generateExecutionId());
    execution.setCustomerId(customerId);
    execution.setName(test.getName());
    execution.setType(executionRequest.getType());
    execution.setExecutionStatus(ExecutionStatus.PENDING);
    execution.setRegionName(regionDetails.getName());
    execution.setTestId(executionRequest.getTestId());
    execution.setLastModified(Instant.now());
    execution.setCreated(Instant.now());
    execution.setTtl(ExecutionUtil.getDefaultExecutionExpirationTime());

    executionRepository.create(execution);

    Map<String, String> payload = new HashMap<>();
    payload.putAll(taskLaunchArgumentsService
        .getExecutionIdArgument(execution.getId()));
    payload.putAll(taskLaunchArgumentsService
        .getExecutionTypeArgument(ExecutionType.MANUAL_EXECUTION.getName()));

    try {
      executionClient.execute(regionDetails.getAwsCloudRegion(), payload);
    } catch (Exception exception) {
      log.error("Execution was not created for {}", execution.getId(), exception);
      executionRepository.delete(execution.getId());
      throw exception;
    }

    return ExecutionRequest.builder()
        .id(execution.getId())
        .region(ObjectBuilder.buildRegion(execution.getRegionName(), regionByName))
        .testId(execution.getTestId())
        .name(execution.getName())
        .startDateTime(execution.getCreated())
        .build();
  }

  @Override
  public PageRequest<ExecutionRequest> getAllExecutions(
      Integer maxResults,
      String nextTokenAsString,
      String customerId) {
    NextToken nextToken = nextTokenConverter.fromString(nextTokenAsString);
    ApiValidationUtil.validateNextTokenInListByCustomerId(nextToken);

    Page<Execution> executions = executionRepository.listExecutionsByCustomerId(
        customerId,
        PersistenceUtil.buildLastEvaluatedKeyInListByCustomerId(nextToken, customerId),
        maxResults);
    Map<String, RegionDetails> regionByName = countryConfigProvider.getRegions();
    List<ExecutionRequest> items = executions.items().stream()
        .map(execution -> ExecutionRequest.builder()
            .id(execution.getId())
            .name(execution.getName())
            .type(execution.getType())
            .executionStatus(execution.getExecutionStatus())
            .region(ObjectBuilder.buildRegion(execution.getRegionName(), regionByName))
            .startDateTime(execution.getCreated())
            .build())
        .collect(Collectors.toList());
    return PageRequest.<ExecutionRequest>builder()
        .items(items)
        .nextToken(nextTokenConverter.toString(
            PersistenceUtil.buildNextTokenInListByCustomerId(executions.lastEvaluatedKey())))
        .build();
  }

  @Override
  public PageRequest<ExecutionRequest> getExecutionsByScheduleId(
      String scheduleId,
      Integer maxResults,
      String nextTokenAsString,
      String customerId) {
    Schedule schedule = scheduleRepository.get(scheduleId);
    if (Objects.isNull(schedule)) {
      throw new ApiException("The schedule was not found!", HttpStatus.BAD_REQUEST.value());
    }
    if (!schedule.getCustomerId().equals(customerId)) {
      throw new ApiException(UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED.value());
    }
    NextToken nextToken = nextTokenConverter.fromString(nextTokenAsString);
    ApiValidationUtil.validateNextTokenInListByScheduleId(nextToken);

    Page<Execution> executions = executionRepository.listExecutionsByScheduleId(
        scheduleId,
        PersistenceUtil.buildLastEvaluatedKeyInListByScheduleId(nextToken, scheduleId),
        maxResults);
    Map<String, RegionDetails> regionByName = countryConfigProvider.getRegions();
    List<ExecutionRequest> items = executions.items().stream()
        .map(execution -> ExecutionRequest.builder()
            .id(execution.getId())
            .name(execution.getName())
            .type(execution.getType())
            .executionStatus(execution.getExecutionStatus())
            .region(ObjectBuilder.buildRegion(execution.getRegionName(), regionByName))
            .startDateTime(execution.getCreated())
            .build())
        .collect(Collectors.toList());
    return PageRequest.<ExecutionRequest>builder()
        .items(items)
        .nextToken(nextTokenConverter.toString(
            PersistenceUtil.buildNextTokenInListByScheduleId(executions.lastEvaluatedKey())))
        .build();
  }

  @Override
  public ExecutionResult getExecutionResult(String executionId, String customerId) {
    Execution execution = executionRepository.get(executionId);
    if (Objects.isNull(execution)) {
      throw new ApiException("The execution was not found!", HttpStatus.BAD_REQUEST.value());
    }
    if (!execution.getCustomerId().equals(customerId)) {
      throw new ApiException(RestApiConstant.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED.value());
    }
    Map<String, RegionDetails> regionByName = countryConfigProvider.getRegions();
    return ExecutionResult.builder()
        .execution(ExecutionRequest.builder()
            .id(execution.getId())
            .name(execution.getName())
            .testId(execution.getTestId())
            .executionStatus(execution.getExecutionStatus())
            .type(execution.getType())
            .region(ObjectBuilder.buildRegion(execution.getRegionName(), regionByName))
            .startDateTime(execution.getCreated())
            .build())
        .result(Result.builder()
            .testStatus(execution.getTestStatus())
            .logs(logsConverter.fromString(execution.getLogs()))
            .build())
        .build();
  }

}
