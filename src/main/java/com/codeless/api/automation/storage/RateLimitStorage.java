package com.codeless.api.automation.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RateLimitStorage {

  private static final Map<String, RateLimitData> STORAGE = new ConcurrentHashMap<>();

  private  RateLimitStorage() {
    throw new RuntimeException();
  }

  public static RateLimitData addRateLimitData(String username, RateLimitData rateLimitData) {
    return STORAGE.putIfAbsent(username, rateLimitData);
  }

  public static RateLimitData getRateLimitData(String username) {
    return STORAGE.get(username);
  }

  public static RateLimitData removeRateLimitData(String username) {
    return STORAGE.remove(username);
  }

}
