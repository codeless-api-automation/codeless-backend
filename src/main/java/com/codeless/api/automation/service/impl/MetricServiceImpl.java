package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.dto.MetricContext;
import com.codeless.api.automation.dto.Metrics;
import com.codeless.api.automation.dto.PutMetricRequest;
import com.codeless.api.automation.dto.TimeSeriesElement;
import com.codeless.api.automation.entity.Schedule;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.repository.ScheduleRepository;
import com.codeless.api.automation.repository.TimeSeriesRepository;
import com.codeless.api.automation.service.MetricService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.timeseries.TSElement;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetricServiceImpl implements MetricService {

  private static final String KEY_DOES_EXIST_ERROR_MESSAGE = "ERR TSDB: the key does not exist";

  private final TimeSeriesRepository timeSeriesRepository;
  private final ScheduleRepository scheduleRepository;

  @Override
  public Metrics getMetrics(MetricContext metricContext, String customerId) {
    Schedule schedule = scheduleRepository.get(metricContext.getScheduleId());
    if (schedule == null) {
      throw new ApiException("Schedule is not found", HttpStatus.BAD_REQUEST.value());
    }
    if (!schedule.getCustomerId().equals(customerId)) {
      throw new ApiException("Unauthorized to access!", HttpStatus.UNAUTHORIZED.value());
    }

    List<TSElement> tsElements = getTsElements(metricContext, schedule);
    List<TimeSeriesElement> timeSeriesElements = tsElements.stream()
        .map(tsElement -> new TimeSeriesElement(tsElement.getTimestamp(), tsElement.getValue()))
        .collect(Collectors.toList());
    return new Metrics(timeSeriesElements);
  }

  @Override
  @Async("metricTaskExecutor")
  public void putMetric(String scheduleId, PutMetricRequest metricRequest) {
    // do not check if schedule id exists on purpose, not to consume extra read capacity from DDB
    timeSeriesRepository.add(buildKey(scheduleId),
        metricRequest.getTimestamp(),
        metricRequest.getValue(),
        metricRequest.getLabels());
  }

  @Override
  @Async("metricTaskExecutor")
  public void deleteMetric(String scheduleId) {
    try {
      timeSeriesRepository.delete(buildKey(scheduleId));
    } catch (Exception exception) {
      // ignore since it is best effort delete
      log.error("Error occurred while deleting metrics for scheduleId={}", scheduleId, exception);
    }
  }

  private List<TSElement> getTsElements(MetricContext metricContext, Schedule schedule) {
    try {
      return timeSeriesRepository.getRange(buildKey(schedule.getId()),
          metricContext.getStartDate().toInstant().getEpochSecond() * 1000,
          metricContext.getEndDate().toInstant().getEpochSecond() * 1000);
    } catch (JedisDataException jedisDataException) {
      if (jedisDataException.getMessage().equals(KEY_DOES_EXIST_ERROR_MESSAGE)) {
        return Collections.emptyList();
      }
      throw jedisDataException;
    }
  }

  private String buildKey(String scheduleId) {
    return String.format("id=%s", scheduleId);
  }

}
