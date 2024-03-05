package com.codeless.api.automation.configuration.interceptor;

import com.codeless.api.automation.configuration.RateLimitConfiguration;
import com.codeless.api.automation.constant.RateLimitHeader;
import com.codeless.api.automation.service.PricingPlanService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthRateLimitRequestInterceptor implements HandlerInterceptor {

  private final RateLimitConfiguration rateLimitConfiguration;
  private final PricingPlanService pricingPlanService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object object) {
    final String ip = request.getRemoteAddr();
    if (rateLimitConfiguration.getNotAllowedIps().contains(ip)) {
      RateLimitErrorHandler.handleForbiddenError(response,
          RateLimitHeader.X_RATE_LIMIT_IP_NOT_ALLOWED, ip);
      return false;
    }

    final Bucket tokenBucket = pricingPlanService.resolveBucketByIp(ip, request.getRequestURI());
    final ConsumptionProbe consumptionProbe = tokenBucket.tryConsumeAndReturnRemaining(1);
    if (consumptionProbe.isConsumed()) {
      response.addHeader(RateLimitHeader.X_RATE_LIMIT_REMAINING,
          String.valueOf(consumptionProbe.getRemainingTokens()));
      return true;
    }
    RateLimitErrorHandler.handleTooManyError(response, consumptionProbe);
    return false;
  }
}
