package com.codeless.api.automation.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "dataflow")
@Configuration
@Data
public class DataFlowConfiguration {

  private String baseURI;
  private String taskName;
  private String definitionName;
}
