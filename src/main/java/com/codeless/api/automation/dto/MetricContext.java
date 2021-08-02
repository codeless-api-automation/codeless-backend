package com.codeless.api.automation.dto;

import java.util.Date;
import lombok.Data;

@Data
public class MetricContext {

  private String scheduleName;
  private Date startDate;
  private Date endDate;
}
