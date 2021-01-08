package com.codeless.api.automation.context;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CronExpressionContext {

  String type;
  String minute;
  String hour;
  String week;
  String time;

}
