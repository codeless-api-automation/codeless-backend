package com.codeless.api.automation.configuration;

import com.codeless.api.automation.converter.EmailListConverter;
import com.codeless.api.automation.converter.LogsConverter;
import com.codeless.api.automation.converter.NextTokenConverter;
import com.codeless.api.automation.converter.TestConverter;
import com.codeless.api.automation.converter.TimerConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterConfiguration {

  @Bean
  public EmailListConverter emailListConverter(ObjectMapper objectMapper) {
    return new EmailListConverter(objectMapper);
  }

  @Bean
  public NextTokenConverter nextTokenConverter(ObjectMapper objectMapper) {
    return new NextTokenConverter(objectMapper);
  }

  @Bean
  public TestConverter testConverter(ObjectMapper objectMapper) {
    return new TestConverter(objectMapper);
  }

  @Bean
  public TimerConverter timerConverter(ObjectMapper objectMapper) {
    return new TimerConverter(objectMapper);
  }

  @Bean
  public LogsConverter logsConverter(ObjectMapper objectMapper) {
    return new LogsConverter(objectMapper);
  }
}
