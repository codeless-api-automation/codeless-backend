package com.codeless.api.automation.dto;

import java.util.Date;
import lombok.Data;

@Data
public class MetricContext {

  private String scheduleId;
  private Date startDate;
  private Date endDate;
}
