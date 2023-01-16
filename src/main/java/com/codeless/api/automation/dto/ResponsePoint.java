package com.codeless.api.automation.dto;

import java.time.Instant;
import lombok.Data;

@Data
public class ResponsePoint {

  private Instant time;

  private String totalResponseTime;

}
