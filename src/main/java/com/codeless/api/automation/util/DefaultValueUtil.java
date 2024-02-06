package com.codeless.api.automation.util;

public final class DefaultValueUtil {

  private DefaultValueUtil() {
    throw new RuntimeException();
  }

  public static int getMaxResultsOrDefault(int maxResults) {
    return Math.min(maxResults, 25);
  }
}
