package com.codeless.api.automation.util;

public final class DefaultValueUtil {

  public static final int MAX_RESULTS = 25;

  private DefaultValueUtil() {
    throw new RuntimeException();
  }

  public static int getMaxResultsOrDefault(int maxResults) {
    return Math.min(maxResults, MAX_RESULTS);
  }
}
