package com.codeless.api.automation.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@EnableAsync
@Configuration
public class AsyncConfiguration {

  @Bean
  public ThreadPoolTaskExecutor emailTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(2);
    executor.setMaxPoolSize(2);
    executor.setQueueCapacity(500);
    executor.setThreadNamePrefix("AsyncEmailThread-");
    executor.setRejectedExecutionHandler(
        (r, executor1) ->
            log.warn("Task rejected, send email thread pool is full and queue is also full"));
    executor.initialize();
    return executor;
  }

  @Bean
  public ThreadPoolTaskExecutor metricTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(2);
    executor.setMaxPoolSize(5);
    executor.setQueueCapacity(1000);
    executor.setThreadNamePrefix("AsyncMetricThread-");
    executor.setRejectedExecutionHandler(
        (r, executor1) ->
            log.warn("Task rejected, put metric thread pool is full and queue is also full"));
    executor.initialize();
    return executor;
  }
}
