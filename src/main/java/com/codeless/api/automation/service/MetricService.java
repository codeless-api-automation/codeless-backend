package com.codeless.api.automation.service;

import com.codeless.api.automation.dto.MetricContext;
import com.codeless.api.automation.dto.Metrics;
import com.codeless.api.automation.dto.PutMetricRequest;

public interface MetricService {

  Metrics getMetrics(MetricContext metricContext, String customerId);

  void putMetrics(String scheduleId, PutMetricRequest metricRequest);
}
