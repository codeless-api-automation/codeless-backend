package com.codeless.api.automation.mapper;

import com.codeless.api.automation.entity.Execution;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExecutionDtoMapper implements
    Mapper<com.codeless.api.automation.dto.request.Execution, Execution> {

  @Autowired
  private TestDtoMapper testDtoMapper;

  @Override
  public Execution map(com.codeless.api.automation.dto.request.Execution source) {
    Execution preparedExecution = new Execution();
    preparedExecution.setRegion(null);
    preparedExecution.setTests(source.getTests().stream()
        .map(testDtoMapper::map)
        .collect(Collectors.toList()));
    return preparedExecution;
  }
}
