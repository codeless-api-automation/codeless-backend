package com.codeless.api.automation.configuration;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxdbConfiguration {

  @Value("${influxdb.database-url}")
  private String databaseURL;

  @Value("${influxdb.user-name}")
  private String userName;

  @Value("${influxdb.password}")
  private String password;

  @Value("${influxdb.database}")
  private String database;

  @Bean
  public InfluxDB influxDatabase() {
    InfluxDB influxDB = InfluxDBFactory.connect(databaseURL, userName, password);
    influxDB.setDatabase(database);
    return influxDB;
  }
}
