package com.codeless.api.automation.util;

public final class ValidationUtil {

  private ValidationUtil ()  {}

  public static int validateMaxResults(int maxResults){
    return Math.min(maxResults, 50);
  }
}
