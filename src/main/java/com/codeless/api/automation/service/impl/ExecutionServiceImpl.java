package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.domain.Test;
import com.codeless.api.automation.dto.Execution;
import com.codeless.api.automation.dto.ExecutionResult;
import com.codeless.api.automation.dto.Page;
import com.codeless.api.automation.entity.ExecutionStatus;
import com.codeless.api.automation.entity.ExecutionType;
import com.codeless.api.automation.entity.Schedule;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.mapper.ExecutionDtoMapper;
import com.codeless.api.automation.mapper.ExecutionMapper;
import com.codeless.api.automation.mapper.ExecutionResultMapper;
import com.codeless.api.automation.repository.ExecutionRepository;
import com.codeless.api.automation.repository.ScheduleRepository;
import com.codeless.api.automation.repository.TestRepository;
import com.codeless.api.automation.service.ExecutionClient;
import com.codeless.api.automation.service.ExecutionService;
import com.codeless.api.automation.service.TestSuiteBuilderService;
import com.codeless.api.automation.util.TaskLaunchArgumentsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExecutionServiceImpl implements ExecutionService {

  private final ExecutionClient executionClient;
  private final TaskLaunchArgumentsService taskLaunchArgumentsService;
  private final TestSuiteBuilderService testSuiteBuilderService;
  private final ExecutionRepository executionRepository;
  private final ExecutionDtoMapper executionDtoMapper;
  private final ExecutionMapper executionMapper;
  private final ExecutionResultMapper executionResultMapper;
  private final TestRepository testRepository;
  private final ScheduleRepository scheduleRepository;
  private final ObjectMapper objectMapper;

  @Override
  @Transactional
  public Execution runExecution(Execution execution, Principal principal) {
    com.codeless.api.automation.entity.Test test = testRepository.findById(execution.getTestId())
        .orElseThrow(() -> new ApiException("Test is not found", HttpStatus.BAD_REQUEST.value()));

    com.codeless.api.automation.entity.Execution preparedExecution =
        executionDtoMapper.map(execution);
    preparedExecution.setStatus(ExecutionStatus.PENDING);
    preparedExecution.setUsername(principal.getName());

    com.codeless.api.automation.entity.Execution persistedExecution =
        executionRepository.save(preparedExecution);

    Map<String, String> payload = new HashMap<>();
    payload.putAll(taskLaunchArgumentsService
        .getTestSuiteArgument(testSuiteBuilderService.build(toTests(test.getJson()))));
    payload.putAll(taskLaunchArgumentsService
        .getExecutionIdArgument(persistedExecution.getId()));
    payload.putAll(taskLaunchArgumentsService
        .getExecutionTypeArgument(ExecutionType.MANUAL_EXECUTION.getName()));

    testingScheduleLocally(execution, persistedExecution, payload, principal);

    executionClient.execute(persistedExecution.getRegion().getAwsCloudRegion(), payload);

    return Execution.builder()
        .id(persistedExecution.getId())
        .region(execution.getRegion())
        .testId(execution.getTestId())
        .name(execution.getName())
        .build();
  }

  @Override
  public Page<Execution> getExecutions(Integer page, Integer size, Principal principal) {
    org.springframework.data.domain.Page<com.codeless.api.automation.entity.Execution> executions =
        executionRepository.findAllByUsername(principal.getName(), PageRequest.of(page, size));

    List<Execution> executionsDto = executions.getContent().stream()
        .map(executionMapper::map)
        .collect(Collectors.toList());
    return Page.<Execution>builder()
        .items(executionsDto)
        .build();
  }

  @Override
  public ExecutionResult getExecutionResult(long executionId, Principal principal) {
    com.codeless.api.automation.entity.Execution execution = executionRepository
        .findByIdAndUsername(executionId, principal.getName())
        .orElseThrow(
            () -> new ApiException("The execution is not found!", HttpStatus.BAD_REQUEST.value()));
    return executionResultMapper.map(execution);
  }

  private List<Test> toTests(String json) {
    try {
      return objectMapper.readValue(json, new TypeReference<List<Test>>(){});
    } catch (Exception exception) {
      throw new RuntimeException();
    }
  }

  private void testingScheduleLocally(
      Execution execution,
      com.codeless.api.automation.entity.Execution persistedExecution,
      Map<String, String> payload,
      Principal principal) {
    // TODO: remove it before PROD
    // scheduler does not work locally so emulating manual execution as if it is scheduled
    if (execution.getName().contains("schedule")) {
      payload.putAll(taskLaunchArgumentsService
          .getExecutionTypeArgument(ExecutionType.SCHEDULED_EXECUTION.getName()));
      List<Schedule> schedules =
          scheduleRepository.findAllByTestIdAndUsername(persistedExecution.getTestId(), principal.getName());
      payload.putAll(taskLaunchArgumentsService
          .getScheduleIdArgument(schedules.get(0).getId()));
    }
  }
}
