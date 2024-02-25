package com.codeless.api.automation.configuration.interceptor;

import com.codeless.api.automation.constant.RateLimitHeader;
import io.github.bucket4j.ConsumptionProbe;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

public final class RateLimitErrorHandler {

  private RateLimitErrorHandler() {
    throw new RuntimeException();
  }

  public static void handleTooManyError(HttpServletResponse response,
      ConsumptionProbe consumptionProbe) {
    final long waitForRefill = consumptionProbe.getNanosToWaitForRefill() / 1_000_000_000;
    response.addHeader(RateLimitHeader.X_RATE_LIMIT_RETRY_AFTER_SECONDS,
        String.valueOf(waitForRefill));
    try {
      response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
          String.format(
              "You have exhausted your API Request Quota, please try again in [%d] seconds.",
              waitForRefill));
    } catch (Exception ex) {
      // Do Nothing, just return false
    }
  }

  public static void handleForbiddenError(HttpServletResponse response, String header,
      String headerValue) {
    response.addHeader(header, headerValue);
    try {
      response.sendError(HttpStatus.FORBIDDEN.value(), "Forbidden");

    } catch (Exception ex) {
      // Do Nothing, just return false
    }
  }
}
