package com.codeless.api.automation.mapper;

import com.codeless.api.automation.dto.Timer;
import com.codeless.api.automation.entity.Schedule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleMapper implements Mapper<Schedule, com.codeless.api.automation.dto.Schedule> {

  private final ObjectMapper objectMapper;
  private final RegionMapper regionMapper;

  @Override
  @SneakyThrows
  public com.codeless.api.automation.dto.Schedule map(Schedule source) {
    return com.codeless.api.automation.dto.Schedule.builder()
        .id(source.getId())
        .scheduleName(source.getName())
        .region(regionMapper.map(source.getRegion()))
        .emails(source.getEmails() == null ? Collections.emptyList()
            : objectMapper.readValue(source.getEmails(), new TypeReference<List<String>>() {
            }))
        .timer(objectMapper.readValue(source.getTimer(), Timer.class))
        .testId(source.getTestId())
        .build();
  }
}
