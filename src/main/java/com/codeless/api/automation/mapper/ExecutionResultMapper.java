package com.codeless.api.automation.mapper;

import com.codeless.api.automation.dto.ExecutionResult;
import com.codeless.api.automation.entity.Execution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExecutionResultMapper implements Mapper<Execution, ExecutionResult> {

  private final ExecutionMapper executionMapper;
  private final ResultMapper resultMapper;

  @Override
  public ExecutionResult map(Execution source) {
    return ExecutionResult.builder()
        .execution(executionMapper.map(source))
        .result(resultMapper.map(source.getResult()))
        .build();
  }
}
