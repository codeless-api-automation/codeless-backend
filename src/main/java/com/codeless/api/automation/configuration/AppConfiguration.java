package com.codeless.api.automation.configuration;

import com.codeless.api.automation.service.TestSuiteBuilderService;
import com.codeless.api.automation.service.TestSuiteBuilderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

  @Bean
  public TestSuiteBuilderService testSuiteBuilderService() {
    return new TestSuiteBuilderServiceImpl();
  }
}
