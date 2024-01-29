package com.codeless.api.automation.service;

import com.codeless.api.automation.dto.MetricContext;
import com.codeless.api.automation.dto.Metrics;

public interface MetricService {

  Metrics getMetrics(MetricContext metricContext, String customerId);
}
