package com.codeless.api.automation.mapper;

import com.codeless.api.automation.dto.Schedule;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleDtoMapper implements
    Mapper<Schedule, com.codeless.api.automation.entity.Schedule> {

  private final ObjectMapper objectMapper;
  private final RegionDtoMapper regionDtoMapper;
  private final TestDtoToTestMapper testDtoToTestMapper;

  @Override
  @SneakyThrows
  public com.codeless.api.automation.entity.Schedule map(Schedule source) {
    com.codeless.api.automation.entity.Schedule scheduleEntity = new com.codeless.api.automation.entity.Schedule();
    scheduleEntity.setName(source.getScheduleName());
    scheduleEntity.setTimer(objectMapper.writeValueAsString(source.getTimer()));
    scheduleEntity.setRegion(regionDtoMapper.map(source.getRegion()));
    scheduleEntity.setTests(source.getTests().stream()
        .map(testDtoToTestMapper::map)
        .collect(Collectors.toList()));
    return scheduleEntity;
  }
}
