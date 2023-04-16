package com.codeless.api.automation.repository;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.timeseries.TimeSeriesProtocol.TimeSeriesCommand;

@Component
@RequiredArgsConstructor
public class TimeSeriesRepository {

  private final JedisPool jedisPool;

  public void create(String key) {
    this.create(key, 0);
  }

  public void create(String key, Integer retentionPeriodInMills) {
    try (Jedis jedis = jedisPool.getResource()) {
      jedis.sendCommand(TimeSeriesCommand.CREATE, key, "RETENTION",
          String.valueOf(retentionPeriodInMills));
    }
  }

  public void add(String key, Long timestamp, Double value, Map<String, String> labels) {
    try (Jedis jedis = jedisPool.getResource()) {
      jedis.sendCommand(TimeSeriesCommand.ADD, key,
          String.valueOf(timestamp),
          String.valueOf(value));
    }
  }

  public Object getRange(String key, Long start, Long end) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.sendCommand(TimeSeriesCommand.RANGE, key,
          String.valueOf(start),
          String.valueOf(end));
    }
  }

}
