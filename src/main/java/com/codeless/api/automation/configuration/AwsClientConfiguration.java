package com.codeless.api.automation.configuration;

import com.google.common.collect.ImmutableSet;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.scheduler.SchedulerClient;

@Configuration
public class AwsClientConfiguration {

  private static final Set<Region> SUPPORTED_REGIONS = ImmutableSet.of(Region.US_EAST_1);

  @Bean(name = "lambdaClientByRegion")
  @Profile("!local")
  public Map<Region, LambdaClient> lambdaClientByRegionProd() {
    return getLambdaClientByRegion(this::getLambdaClientForProd);
  }

  private LambdaClient getLambdaClientForProd(Region region) {
    return LambdaClient.builder()
        .region(region)
        .build();
  }

  @Bean(name = "lambdaClientByRegion")
  @Profile("local")
  public Map<Region, LambdaClient> lambdaClientByRegionLocal() {
    return getLambdaClientByRegion(this::getLambdaClientForLocalStack);
  }

  private Map<Region, LambdaClient> getLambdaClientByRegion(
      Function<Region, LambdaClient> lambdaCreator) {
    Map<Region, LambdaClient> lambdaClientByRegion = new HashMap<>();
    for (Region region : SUPPORTED_REGIONS) {
      lambdaClientByRegion.put(region, lambdaCreator.apply(region));
    }
    return lambdaClientByRegion;
  }

  private LambdaClient getLambdaClientForLocalStack(Region region) {
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


  @Bean(name = "dynamoDbEnhancedClient")
  @Profile("!local")
  public DynamoDbEnhancedClient dynamoDbEnhancedClientProd() {
    return DynamoDbEnhancedClient.builder()
        .dynamoDbClient(getDynamoDbClientProd())
        .build();
  }

  private DynamoDbClient getDynamoDbClientProd() {
    return DynamoDbClient.builder()
        .build();
  }

  @Bean(name = "dynamoDbEnhancedClient")
  @Profile("local")
  public DynamoDbEnhancedClient dynamoDbEnhancedClientLocal() {
    return DynamoDbEnhancedClient.builder()
        .dynamoDbClient(getDynamoDbClientLocal()).build();
  }

  private DynamoDbClient getDynamoDbClientLocal() {
    AwsCredentials credentials = AwsBasicCredentials.create("1", "2");
    return DynamoDbClient.builder()
        .applyMutation(builder -> builder.endpointOverride(URI.create("http://localhost:4566")))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }

}
