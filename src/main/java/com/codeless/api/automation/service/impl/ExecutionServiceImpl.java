package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.appconfig.CountryConfig.RegionDetails;
import com.codeless.api.automation.appconfig.CountryConfigProvider;
import com.codeless.api.automation.converter.LogsConverter;
import com.codeless.api.automation.converter.NextTokenConverter;
import com.codeless.api.automation.domain.Test;
import com.codeless.api.automation.dto.ExecutionRequest;
import com.codeless.api.automation.dto.ExecutionResult;
import com.codeless.api.automation.dto.PageRequest;
import com.codeless.api.automation.dto.Result;
import com.codeless.api.automation.entity.Execution;
import com.codeless.api.automation.entity.enums.ExecutionStatus;
import com.codeless.api.automation.entity.enums.ExecutionType;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.repository.ExecutionRepository;
import com.codeless.api.automation.repository.TestRepository;
import com.codeless.api.automation.service.ExecutionClient;
import com.codeless.api.automation.service.ExecutionService;
import com.codeless.api.automation.service.TestSuiteBuilderService;
import com.codeless.api.automation.util.ObjectBuilder;
import com.codeless.api.automation.util.RandomIdGenerator;
import com.codeless.api.automation.util.TaskLaunchArgumentsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

@Service
@RequiredArgsConstructor
public class ExecutionServiceImpl implements ExecutionService {

  private final ExecutionClient executionClient;
  private final TaskLaunchArgumentsService taskLaunchArgumentsService;
  private final TestSuiteBuilderService testSuiteBuilderService;
  private final ExecutionRepository executionRepository;
  private final LogsConverter logsConverter;
  private final NextTokenConverter nextTokenConverter;
  private final TestRepository testRepository;
  private final CountryConfigProvider countryConfigProvider;
  private final ObjectMapper objectMapper;

  @Override
  public ExecutionRequest createExecution(ExecutionRequest executionRequest, String customerId) {
    com.codeless.api.automation.entity.Test test = testRepository.get(executionRequest.getTestId());
    if (test == null) {
      throw new ApiException("Test is not found", HttpStatus.BAD_REQUEST.value());
    }
    if (test.getCustomerId().equals(customerId)) {
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
    execution.setName(executionRequest.getName());
    execution.setType(executionRequest.getType());
    execution.setExecutionStatus(ExecutionStatus.PENDING);
    execution.setRegionName(regionDetails.getName());
    execution.setTestId(executionRequest.getTestId());

    executionRepository.create(execution);

    Map<String, String> payload = new HashMap<>();
    payload.putAll(taskLaunchArgumentsService
        .getTestSuiteArgument(testSuiteBuilderService.build(toTests(test.getJson()))));
    payload.putAll(taskLaunchArgumentsService
        .getExecutionIdArgument(execution.getId()));
    payload.putAll(taskLaunchArgumentsService
        .getExecutionTypeArgument(ExecutionType.MANUAL_EXECUTION.getName()));

    executionClient.execute(regionDetails.getAwsCloudRegion(), payload);

    return ExecutionRequest.builder()
        .id(execution.getId())
        .region(ObjectBuilder.buildRegion(execution.getRegionName(), regionByName))
        .testId(execution.getTestId())
        .name(execution.getName())
        .build();
  }

  @Override
  public PageRequest<ExecutionRequest> getAllExecutions(
      Integer maxResults,
      String nextToken,
      String customerId) {
    Page<Execution> executions = executionRepository.listExecutionsByCustomerId(
        customerId,
        nextTokenConverter.fromString(nextToken),
        maxResults);
    Map<String, RegionDetails> regionByName = countryConfigProvider.getRegions();
    List<ExecutionRequest> items = executions.items().stream()
        .map(execution -> ExecutionRequest.builder()
            .id(execution.getId())
            .name(execution.getName())
            .type(execution.getType())
            .testId(execution.getTestId())
            .region(ObjectBuilder.buildRegion(execution.getRegionName(), regionByName))
            .build())
        .collect(Collectors.toList());
    return PageRequest.<ExecutionRequest>builder()
        .items(items)
        .nextToken(nextTokenConverter.toString(executions.lastEvaluatedKey()))
        .build();
  }

  @Override
  public ExecutionResult getExecutionResult(String executionId, String customerId) {
    Execution execution = executionRepository.get(executionId);
    if (Objects.isNull(execution)) {
      throw new ApiException("The execution was not found!", HttpStatus.BAD_REQUEST.value());
    }
    if (!execution.getCustomerId().equals(customerId)) {
      throw new ApiException("Unauthorized to access!", HttpStatus.UNAUTHORIZED.value());
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
            .build())
        .result(Result.builder()
            .testStatus(execution.getTestStatus())
            .logs(logsConverter.fromString(execution.getLogs()))
            .build())
        .build();
  }

  private List<Test> toTests(String json) {
    try {
      return objectMapper.readValue(json, new TypeReference<List<Test>>(){});
    } catch (Exception exception) {
      throw new RuntimeException();
    }
  }

}
