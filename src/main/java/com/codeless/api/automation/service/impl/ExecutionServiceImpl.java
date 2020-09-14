package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.configuration.DataFlowConfiguration;
import com.codeless.api.automation.domain.Test;
import com.codeless.api.automation.dto.request.Execution;
import com.codeless.api.automation.mapper.ExecutionDtoMapper;
import com.codeless.api.automation.mapper.TestDtoToTestDomainMapper;
import com.codeless.api.automation.repository.ExecutionRepository;
import com.codeless.api.automation.service.ExecutionService;
import com.codeless.api.automation.service.TestSuiteBuilderService;
import com.google.common.collect.ImmutableList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.cloud.dataflow.rest.client.TaskOperations;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExecutionServiceImpl implements ExecutionService {

  private final TaskOperations taskOperations;
  private final DataFlowConfiguration dataFlowConfiguration;
  private final TestDtoToTestDomainMapper testDtoToTestDomainMapper;
  private final TestSuiteBuilderService testSuiteBuilderService;
  private final ExecutionRepository executionRepository;
  private final ExecutionDtoMapper executionDtoMapper;

  @Override
  public Execution runExecution(Execution execution) {
    List<Test> tests = execution.getTests().stream()
        .map(testDtoToTestDomainMapper::map)
        .collect(Collectors.toList());

    long executionId = taskOperations.launch(dataFlowConfiguration.getTestExecutionTaskName(),
        Collections.emptyMap(),
        ImmutableList.of(getTestSuiteArgument(testSuiteBuilderService.build(tests))),
        null);

    com.codeless.api.automation.entity.Execution preparedExecution =
        executionDtoMapper.map(execution);
    preparedExecution.setExecutionId(executionId);

    executionRepository.save(preparedExecution);
    return Execution.builder()
        .region(execution.getRegion())
        .tests(execution.getTests())
        .executionId(executionId)
        .build();
  }

  private String getTestSuiteArgument(String testSuite) {
    byte[] encodedTestSuite = Base64.getEncoder().encode(testSuite.getBytes());
    final String testSuiteTaskArgument = "suite=%s";
    return String.format(testSuiteTaskArgument, new String(encodedTestSuite));
  }
}
