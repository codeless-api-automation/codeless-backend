package com.codeless.api.automation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimeSeriesElement {

  private final long timestamp;
  private final double value;

}
