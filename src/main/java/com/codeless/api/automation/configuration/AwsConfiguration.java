package com.codeless.api.automation.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AwsConfiguration {

  @Value("${codeless.aws.function-name}")
  private String functionName;
  @Value("${codeless.aws.schedule-role-name}")
  private String scheduleRoleName;
}
