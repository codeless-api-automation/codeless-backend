package com.codeless.api.automation.configuration;

import com.codeless.api.automation.appconfig.CountryConfigProvider;
import com.codeless.api.automation.appconfig.LimitsConfigProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class ConfigProviderConfiguration {

  @Bean
  public CountryConfigProvider countryConfigProvider(
      ResourceLoader resourceLoader,
      ObjectMapper objectMapper) {
    return new CountryConfigProvider(resourceLoader, objectMapper);
  }

  @Bean
  public LimitsConfigProvider limitsConfigProvider(
      ResourceLoader resourceLoader,
      ObjectMapper objectMapper) {
    return new LimitsConfigProvider(resourceLoader, objectMapper);
  }
}
