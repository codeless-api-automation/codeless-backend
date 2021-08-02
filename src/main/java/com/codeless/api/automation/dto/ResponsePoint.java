package com.codeless.api.automation.dto;

import java.time.Instant;
import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "response_time")
@Data
public class ResponsePoint {

  @Column(name = "time")
  private Instant time;

  @Column(name = "total_response_time")
  private String totalResponseTime;

}
