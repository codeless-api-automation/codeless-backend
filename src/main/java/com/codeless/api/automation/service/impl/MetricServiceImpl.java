package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.dto.MetricContext;
import com.codeless.api.automation.dto.Metrics;
import com.codeless.api.automation.dto.ResponsePoint;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.service.MetricService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class MetricServiceImpl implements MetricService {

  private final InfluxDB influxDatabase;

  @Override
  public Metrics getMetrics(MetricContext metricContext) {

    String queryFormat = String.format(""
            + "SELECT * FROM response_time WHERE schedule_name = '%s' AND time > '%s' AND time < '%s'",
        metricContext.getScheduleName(),
        metricContext.getStartDate().toInstant().toString(),
        metricContext.getEndDate().toInstant().toString());

    log.info("Metric query={}", queryFormat);
    List<ResponsePoint> memoryPoints;
    try (InfluxDB influxDB = influxDatabase) {
      QueryResult queryResult = influxDB.query(new Query(queryFormat));

      if (queryResult.hasError()) {
        log.error(queryResult.getError());
        throw new ApiException("Error occurred while fetching metrics!",
            HttpStatus.INTERNAL_SERVER_ERROR.value());
      }

      InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
      memoryPoints = resultMapper.toPOJO(queryResult, ResponsePoint.class);
    }

    return new Metrics(memoryPoints);
  }
}
