package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.configuration.DataFlowConfiguration;
import com.codeless.api.automation.domain.Test;
import com.codeless.api.automation.dto.Execution;
import com.codeless.api.automation.dto.ExecutionResult;
import com.codeless.api.automation.dto.Page;
import com.codeless.api.automation.entity.ExecutionStatus;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.mapper.ExecutionDtoMapper;
import com.codeless.api.automation.mapper.ExecutionMapper;
import com.codeless.api.automation.mapper.ExecutionResultMapper;
import com.codeless.api.automation.mapper.TestDtoToTestDomainMapper;
import com.codeless.api.automation.repository.ExecutionRepository;
import com.codeless.api.automation.service.ExecutionService;
import com.codeless.api.automation.service.TestSuiteBuilderService;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.dataflow.rest.client.TaskOperations;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExecutionServiceImpl implements ExecutionService {

  private static final String BACK_OFF_LIMIT = "deployer.%s.kubernetes.backoffLimit";

  private final TaskOperations taskOperations;
  private final DataFlowConfiguration dataFlowConfiguration;
  private final TestDtoToTestDomainMapper testDtoToTestDomainMapper;
  private final TestSuiteBuilderService testSuiteBuilderService;
  private final ExecutionRepository executionRepository;
  private final ExecutionDtoMapper executionDtoMapper;
  private final ExecutionMapper executionMapper;
  private final ExecutionResultMapper executionResultMapper;

  @Override
  public Execution runExecution(Execution execution) {
    List<Test> tests = execution.getTests().stream()
        .map(testDtoToTestDomainMapper::map)
        .collect(Collectors.toList());

    long executionId = taskOperations.launch(dataFlowConfiguration.getTaskName(),
        getProperties(),
        ImmutableList.of(getTestSuiteArgument(testSuiteBuilderService.build(tests))),
        null);

    com.codeless.api.automation.entity.Execution preparedExecution =
        executionDtoMapper.map(execution);
    preparedExecution.setStatus(ExecutionStatus.PENDING);
    preparedExecution.setExecutionId(executionId);

    executionRepository.save(preparedExecution);
    return Execution.builder()
        .region(execution.getRegion())
        .tests(execution.getTests())
        .executionId(executionId)
        .name(execution.getName())
        .build();
  }

  @Override
  public Page<Execution> getExecutions(Integer page, Integer size) {
    org.springframework.data.domain.Page<com.codeless.api.automation.entity.Execution> executions =
        executionRepository.findAll(PageRequest.of(page, size));

    List<Execution> executionsDto = executions.getContent().stream()
        .map(executionMapper::map)
        .collect(Collectors.toList());
    return Page.<Execution>builder()
        .items(executionsDto)
        .build();
  }

  @Override
  public ExecutionResult getExecutionResult(long executionId) {
    com.codeless.api.automation.entity.Execution execution = executionRepository
        .findByExecutionId(executionId)
        .orElseThrow(
            () -> new ApiException("The execution is not found!", HttpStatus.BAD_REQUEST.value()));
    return executionResultMapper.map(execution);
  }

  private String getTestSuiteArgument(String testSuite) {
    byte[] encodedTestSuite = Base64.getEncoder().encode(testSuite.getBytes());
    final String testSuiteTaskArgument = "suite=%s";
    return String.format(testSuiteTaskArgument, new String(encodedTestSuite));
  }

  private Map<String, String> getProperties() {
    return ImmutableMap.<String, String>builder()
        .put(String.format(BACK_OFF_LIMIT, dataFlowConfiguration.getDefinitionName()), "0")
        .build();
  }
}
