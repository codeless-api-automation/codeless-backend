package com.codeless.api.automation.service;

import com.codeless.api.automation.context.CronExpressionContext;

public interface CronExpressionBuilderService {

  String buildCronExpression(CronExpressionContext cronExpressionContext);
}
