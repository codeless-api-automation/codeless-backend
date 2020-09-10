package com.codeless.api.automation.configuration;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.dataflow.rest.client.DataFlowOperations;
import org.springframework.cloud.dataflow.rest.client.DataFlowTemplate;
import org.springframework.cloud.dataflow.rest.client.TaskOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataFlowClientConfiguration {

  @Autowired
  private DataFlowConfiguration dataFlowConfiguration;

  @Bean
  public DataFlowOperations dataFlowTemplate() {
    return new DataFlowTemplate(URI.create(dataFlowConfiguration.getBaseURI()));
  }

  @Bean
  public TaskOperations taskOperations(DataFlowOperations dataFlowOperations) {
    return dataFlowOperations.taskOperations();
  }
}
