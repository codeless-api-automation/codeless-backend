package com.codeless.api.automation.constant;

public final class RateLimitHeader {

  public static final String X_RATE_LIMIT_REMAINING = "X-Rate-Limit-Remaining";
  public static final String X_RATE_LIMIT_RETRY_AFTER_SECONDS = "X-Rate-Limit-Retry-After-Seconds";

  private RateLimitHeader() {
    throw new RuntimeException();
  }
}
