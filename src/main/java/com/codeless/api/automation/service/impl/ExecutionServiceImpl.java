package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.configuration.DataFlowConfiguration;
import com.codeless.api.automation.dto.request.Execution;
import com.codeless.api.automation.mapper.ExecutionDtoMapper;
import com.codeless.api.automation.repository.ExecutionRepository;
import com.codeless.api.automation.service.ExecutionService;
import java.util.Collections;
import lombok.AllArgsConstructor;
import org.springframework.cloud.dataflow.rest.client.TaskOperations;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExecutionServiceImpl implements ExecutionService {

  private final DataFlowConfiguration dataFlowConfiguration;
  private final ExecutionRepository executionRepository;
  private final ExecutionDtoMapper executionDtoMapper;
  private final TaskOperations taskOperations;

  @Override
  public Execution runExecution(Execution execution) {
    long executionId = taskOperations.launch(dataFlowConfiguration.getTestExecutionTaskName(),
            Collections.emptyMap(),
            Collections.emptyList(),
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
}
