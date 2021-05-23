package com.codeless.api.automation.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

@EnableRedisHttpSession
@Configuration
public class HttpSessionConfiguration {

  @Bean
  public LettuceConnectionFactory connectionFactory() {
    return new LettuceConnectionFactory();
  }

  @Bean
  public HttpSessionIdResolver httpSessionIdResolver() {
    return HeaderHttpSessionIdResolver.xAuthToken();
  }

}
