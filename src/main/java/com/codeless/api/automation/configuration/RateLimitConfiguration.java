package com.codeless.api.automation.configuration;

import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class RateLimitConfiguration {

  @Value("${codeless.rate.limit.not.allowed.clients}")
  private List<String> notAllowedClients;

  @Value("${codeless.rate.limit.not.allowed.ips}")
  private List<String> notAllowedIps;

}
