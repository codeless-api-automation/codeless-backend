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

  private static final String LOCAL_ENDPOINT_URL = "http://localhost:4566";
  private static final Set<Region> SUPPORTED_REGIONS = ImmutableSet.of(Region.US_EAST_1);

  private static final String ACCESS_KEY = "accessKey";
  private static final String SECRET_KEY = "secretKey";

  @Bean
  public Map<Region, LambdaClient> lambdaClientByRegion(AwsConfiguration awsConfiguration) {
    Map<Region, LambdaClient> lambdaClientByRegion = new HashMap<>();
    for (Region region : SUPPORTED_REGIONS) {
      Map<String, String> regionCredentials = awsConfiguration.getCredentialsByRegion()
          .get(region.toString());
      AwsCredentials credentials = AwsBasicCredentials.create(
          regionCredentials.get(ACCESS_KEY),
          regionCredentials.get(SECRET_KEY));
      LambdaClient lambdaClient = LambdaClient.builder()
          .region(region)
          .credentialsProvider(StaticCredentialsProvider.create(credentials))
          .applyMutation(builder -> builder.endpointOverride(URI.create(LOCAL_ENDPOINT_URL)))
          .build();
      lambdaClientByRegion.put(region, lambdaClient);
    }
    return lambdaClientByRegion;
  }

  @Bean
  public Map<Region, SchedulerClient> schedulerClientByRegion(AwsConfiguration awsConfiguration) {
    Map<Region, SchedulerClient> schedulerClientByRegion = new HashMap<>();
    for (Region region : SUPPORTED_REGIONS) {
      Map<String, String> regionCredentials = awsConfiguration.getCredentialsByRegion()
          .get(region.toString());
      AwsCredentials credentials = AwsBasicCredentials.create(
          regionCredentials.get(ACCESS_KEY),
          regionCredentials.get(SECRET_KEY));
      SchedulerClient schedulerClient = SchedulerClient.builder()
          .region(region)
          .credentialsProvider(StaticCredentialsProvider.create(credentials))
          .applyMutation(builder -> builder.endpointOverride(URI.create(LOCAL_ENDPOINT_URL)))
          .build();
      schedulerClientByRegion.put(region, schedulerClient);
    }
    return schedulerClientByRegion;
  }

}
