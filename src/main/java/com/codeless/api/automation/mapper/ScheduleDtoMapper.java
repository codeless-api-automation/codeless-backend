package com.codeless.api.automation.mapper;

import com.codeless.api.automation.dto.Schedule;
import com.codeless.api.automation.entity.Region;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.repository.RegionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleDtoMapper implements
    Mapper<Schedule, com.codeless.api.automation.entity.Schedule> {

  private final ObjectMapper objectMapper;
  private final RegionRepository regionRepository;

  @Override
  @SneakyThrows
  public com.codeless.api.automation.entity.Schedule map(Schedule source) {
    Region region = regionRepository.findByCity(source.getRegion().getCity())
        .orElseThrow(() -> new ApiException(
            String.format("Region with city name '%s' is not found.", source.getRegion().getCity()),
            HttpStatus.BAD_REQUEST.value()));

    com.codeless.api.automation.entity.Schedule scheduleEntity = new com.codeless.api.automation.entity.Schedule();
    scheduleEntity.setName(source.getScheduleName());
    scheduleEntity.setRegion(region);
    scheduleEntity.setTimer(objectMapper.writeValueAsString(source.getTimer()));
    scheduleEntity.setEmails(
        source.getEmails() == null ? null : objectMapper.writeValueAsString(source.getEmails()));
    scheduleEntity.setTestId(source.getTestId());
    return scheduleEntity;
  }
}
