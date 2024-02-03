package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.context.CronExpressionContext;
import com.codeless.api.automation.service.CronExpressionBuilderService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CronExpressionBuilderServiceImpl implements CronExpressionBuilderService {

  @Value("#{${minute-timer}}")
  private final Map<String, String> minute;
  @Value("#{${hour-timer}}")
  private final Map<String, String> hour;
  @Value("#{${week-timer}}")
  private final Map<String, String> week;
  @Value("#{${time}}")
  private final Map<String, String> time;

  @Override
  public String buildCronExpression(CronExpressionContext cronExpressionContext) {
    switch (cronExpressionContext.getType()) {
      case "MINUTE_TIMER":
        return String.format("%s * ? * * *",
            minute.get(cronExpressionContext.getMinute()));
      case "HOUR_TIMER":
        return String.format("0 %s ? * * *",
            hour.get(cronExpressionContext.getHour()));
      case "WEEK_TIMER":
        return String.format("0 %s ? * %s *",
            time.get(cronExpressionContext.getTime()),
            week.get(cronExpressionContext.getWeek()));
    }
    throw new IllegalArgumentException();
  }

}
