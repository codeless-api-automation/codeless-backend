package com.codeless.api.automation.configuration;

import com.codeless.api.automation.appconfig.CountryConfigProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigProviderConfiguration {

  @Bean
  public CountryConfigProvider countryConfigProvider(ObjectMapper objectMapper) {
    return new CountryConfigProvider(objectMapper);
  }
}
