package com.codeless.api.automation.mapper;

import com.codeless.api.automation.entity.Execution;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExecutionDtoMapper implements
    Mapper<com.codeless.api.automation.dto.Execution, Execution> {

  private final TestDtoToTestMapper testDtoMapper;
  private final RegionDtoMapper regionDtoMapper;

  @Override
  public Execution map(com.codeless.api.automation.dto.Execution source) {
    Execution preparedExecution = new Execution();
    preparedExecution.setRegion(regionDtoMapper.map(source.getRegion()));
    preparedExecution.setTests(source.getTests().stream()
        .map(testDtoMapper::map)
        .collect(Collectors.toList()));
    return preparedExecution;
  }
}
