package com.codeless.api.automation.configuration;

import com.google.common.collect.ImmutableSet;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.scheduler.SchedulerClient;

@Configuration
public class AwsClientConfiguration {

  private static final Set<Region> SUPPORTED_REGIONS = ImmutableSet.of(Region.US_EAST_1);

  @Bean
  public Map<Region, LambdaClient> lambdaClientByRegion() {
    Map<Region, LambdaClient> lambdaClientByRegion = new HashMap<>();
    for (Region region : SUPPORTED_REGIONS) {
      lambdaClientByRegion.put(region, getLambdaClientForProd(region));
    }
    return lambdaClientByRegion;
  }

  public LambdaClient getLambdaClientForProd(Region region) {
    return LambdaClient.builder()
        .region(region)
        .build();
  }

  public LambdaClient getLambdaClientForLocalStack(Region region) {
    AwsCredentials credentials = AwsBasicCredentials.create("1", "2");
    return LambdaClient.builder()
        .region(region)
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .applyMutation(builder -> builder.endpointOverride(URI.create("http://localhost:4566")))
        .build();
  }

  @Bean
  public Map<Region, SchedulerClient> schedulerClientByRegion() {
    Map<Region, SchedulerClient> schedulerClientByRegion = new HashMap<>();
    for (Region region : SUPPORTED_REGIONS) {
      SchedulerClient schedulerClient = SchedulerClient.builder()
          .region(region)
          .build();
      schedulerClientByRegion.put(region, schedulerClient);
    }
    return schedulerClientByRegion;
  }

}
