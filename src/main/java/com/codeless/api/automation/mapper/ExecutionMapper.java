package com.codeless.api.automation.mapper;

import com.codeless.api.automation.entity.Execution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExecutionMapper implements
    Mapper<Execution, com.codeless.api.automation.dto.Execution> {

  private final RegionMapper regionMapper;

  @Override
  public com.codeless.api.automation.dto.Execution map(Execution source) {
    return com.codeless.api.automation.dto.Execution.builder()
        .id(source.getId())
        .executionStatus(source.getStatus())
        .type(source.getType())
        .name(source.getName())
        .region(regionMapper.map(source.getRegion()))
        .testId(source.getTestId())
        .build();
  }
}
