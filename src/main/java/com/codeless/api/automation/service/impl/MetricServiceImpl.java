package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.dto.MetricContext;
import com.codeless.api.automation.dto.Metrics;
import com.codeless.api.automation.dto.TimeSeriesElement;
import com.codeless.api.automation.entity.Schedule;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.repository.ScheduleRepository;
import com.codeless.api.automation.repository.TimeSeriesRepository;
import com.codeless.api.automation.service.MetricService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import redis.clients.jedis.timeseries.TSElement;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetricServiceImpl implements MetricService {

  private final TimeSeriesRepository timeSeriesRepository;
  private final ScheduleRepository scheduleRepository;

  @Override
  public Metrics getMetrics(MetricContext metricContext) {
    Schedule schedule = scheduleRepository.findById(metricContext.getScheduleId())
        .orElseThrow(() -> new ApiException("Schedule is not found", HttpStatus.BAD_REQUEST.value()));

    String key = String.format("uuid=%s", schedule.getUuid());
    List<TSElement> tsElements = timeSeriesRepository.getRange(key,
        metricContext.getStartDate().toInstant().getEpochSecond() * 1000,
        metricContext.getEndDate().toInstant().getEpochSecond() * 1000);

    List<TimeSeriesElement> timeSeriesElements = tsElements.stream()
        .map(tsElement -> new TimeSeriesElement(tsElement.getTimestamp(), tsElement.getValue()))
        .collect(Collectors.toList());

    return new Metrics(timeSeriesElements);
  }
}
