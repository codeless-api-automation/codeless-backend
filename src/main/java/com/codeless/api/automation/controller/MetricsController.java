package com.codeless.api.automation.controller;

import static com.codeless.api.automation.util.RestApiConstant.METRICS_RESOURCE;

import com.codeless.api.automation.dto.MetricContext;
import com.codeless.api.automation.dto.Metrics;
import com.codeless.api.automation.service.MetricService;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(METRICS_RESOURCE)
@Validated
@RequiredArgsConstructor
public class MetricsController {

  private final MetricService metricService;

  @GetMapping
  public Metrics getMetrics(
      @RequestParam(name = "schedule_id") Long scheduleId,
      @RequestParam(name = "start_date", required = false) @DateTimeFormat(iso = ISO.DATE_TIME) Date startDate,
      @RequestParam(name = "end_date", required = false) @DateTimeFormat(iso = ISO.DATE_TIME) Date endDate) {

    MetricContext metricContext = new MetricContext();
    metricContext.setScheduleId(scheduleId);
    metricContext.setStartDate(startDate == null ? new Date() : startDate);
    metricContext.setEndDate(endDate == null ? new Date() : endDate);

    return metricService.getMetrics(metricContext);
  }
}
