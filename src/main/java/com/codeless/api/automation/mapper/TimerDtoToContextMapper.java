package com.codeless.api.automation.mapper;

import com.codeless.api.automation.context.CronExpressionContext;
import com.codeless.api.automation.dto.Timer;
import org.springframework.stereotype.Service;

@Service
public class TimerDtoToContextMapper implements Mapper<Timer, CronExpressionContext> {

  @Override
  public CronExpressionContext map(Timer source) {
    return CronExpressionContext.builder()
        .type(source.getType())
        .minute(source.getMinute())
        .hour(source.getHour())
        .week(source.getWeek())
        .time(source.getTime())
        .build();
  }
}
