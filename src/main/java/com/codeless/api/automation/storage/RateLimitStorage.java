package com.codeless.api.automation.storage;

import java.util.LinkedHashMap;
import java.util.Map;

public final class RateLimitStorage {

  private static final Map<String, RateLimitData> STORAGE = new LinkedHashMap<>(){
    @Override
    protected boolean removeEldestEntry(final Map.Entry eldest) {
      return size() > 1000;
    }
  };

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
