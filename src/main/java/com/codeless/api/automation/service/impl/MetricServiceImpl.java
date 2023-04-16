package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.dto.MetricContext;
import com.codeless.api.automation.dto.Metrics;
import com.codeless.api.automation.dto.ResponsePoint;
import com.codeless.api.automation.repository.TimeSeriesRepository;
import com.codeless.api.automation.service.MetricService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetricServiceImpl implements MetricService {

  private final TimeSeriesRepository timeSeriesRepository;

  @Override
  public Metrics getMetrics(MetricContext metricContext) {
    Object time = timeSeriesRepository.getRange(metricContext.getScheduleName(),
        metricContext.getStartDate().toInstant().getEpochSecond(),
        metricContext.getEndDate().toInstant().getEpochSecond());

    log.info("Time series metrics {}", time);

    List<ResponsePoint> memoryPoints = new ArrayList<>();
    return new Metrics(memoryPoints);
  }
}
