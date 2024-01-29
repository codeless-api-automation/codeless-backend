package com.codeless.api.automation.configuration;

import com.codeless.api.automation.repository.ExecutionRepository;
import com.codeless.api.automation.repository.ScheduleRepository;
import com.codeless.api.automation.repository.TestRepository;
import com.codeless.api.automation.repository.TimeSeriesRepository;
import com.codeless.api.automation.repository.UserRepository;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@Configuration
public class PersistenceConfiguration {

  @Bean
  public TestRepository testRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
    return new TestRepository(dynamoDbEnhancedClient);
  }

  @Bean
  public ScheduleRepository scheduleRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
    return new ScheduleRepository(dynamoDbEnhancedClient);
  }

  @Bean
  public ExecutionRepository executionRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
    return new ExecutionRepository(dynamoDbEnhancedClient);
  }

  @Bean
  public UserRepository userRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
    return new UserRepository(dynamoDbEnhancedClient);
  }

  @Bean
  public TimeSeriesRepository timeSeriesRepository(JedisPooled jedisPooled) {
    return new TimeSeriesRepository(Duration.ofDays(30), jedisPooled);
  }
}
