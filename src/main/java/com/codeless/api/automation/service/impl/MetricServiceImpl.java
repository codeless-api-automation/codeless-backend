package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.dto.MetricContext;
import com.codeless.api.automation.dto.Metrics;
import com.codeless.api.automation.dto.ResponsePoint;
import com.codeless.api.automation.service.MetricService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class MetricServiceImpl implements MetricService {

  @Override
  public Metrics getMetrics(MetricContext metricContext) {
    String queryFormat = String.format(""
            + "SELECT * FROM response_time WHERE schedule_name = '%s' AND time > '%s' AND time < '%s'",
        metricContext.getScheduleName(),
        metricContext.getStartDate().toInstant().toString(),
        metricContext.getEndDate().toInstant().toString());

    log.info("Metric query={}", queryFormat);
    List<ResponsePoint> memoryPoints = new ArrayList<>();
    return new Metrics(memoryPoints);
  }
}
