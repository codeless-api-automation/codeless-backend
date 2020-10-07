package com.codeless.api.automation.mapper;

import com.codeless.api.automation.entity.Execution;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExecutionMapper implements
    Mapper<Execution, com.codeless.api.automation.dto.Execution> {

  private final RegionMapper regionMapper;
  private final TestMapper testMapper;

  @Override
  public com.codeless.api.automation.dto.Execution map(Execution source) {
    return com.codeless.api.automation.dto.Execution.builder()
        .executionId(source.getExecutionId())
        .type(source.getType())
        .name(source.getName())
        .region(regionMapper.map(source.getRegion()))
        .tests(source.getTests().stream().map(testMapper::map).collect(Collectors.toList()))
        .build();
  }
}
