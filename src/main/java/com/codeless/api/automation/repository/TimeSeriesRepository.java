package com.codeless.api.automation.repository;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.timeseries.TSCreateParams;
import redis.clients.jedis.timeseries.TSElement;

@Component
public class TimeSeriesRepository {

  @Value("${codeless.ts.retention}")
  private Duration retention;

  @Autowired
  private JedisPooled jedisPool;

  public void create(String key) {
    this.create(key, retention.toMillis());
  }

  public void create(String key, Long retentionPeriodInMills) {
    TSCreateParams tsCreateParams = new TSCreateParams();
    tsCreateParams.retention(retentionPeriodInMills);
    jedisPool.tsCreate(key, tsCreateParams);
  }

  public void add(String key, Long timestamp, Double value, Map<String, String> labels) {
    TSCreateParams tsCreateParams = new TSCreateParams();
    tsCreateParams.labels(labels);
    tsCreateParams.retention(retention.toMillis());
    jedisPool.tsAdd(key, timestamp, value);
  }

  public List<TSElement> getRange(String key, Long fromTimestamp, Long toTimestamp) {
    return jedisPool.tsRange(key, fromTimestamp, toTimestamp);
  }

}
