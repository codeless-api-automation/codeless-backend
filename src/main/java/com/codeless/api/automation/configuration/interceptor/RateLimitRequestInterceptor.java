package com.codeless.api.automation.configuration.interceptor;

import com.codeless.api.automation.configuration.RateLimitConfiguration;
import com.codeless.api.automation.constant.RateLimitHeader;
import com.codeless.api.automation.entity.User;
import com.codeless.api.automation.service.PricingPlanService;
import com.codeless.api.automation.service.UserService;
import com.codeless.api.automation.storage.RateLimitData;
import com.codeless.api.automation.storage.RateLimitStorage;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitRequestInterceptor implements HandlerInterceptor {

  private final RateLimitConfiguration rateLimitConfiguration;
  private final PricingPlanService pricingPlanService;
  private final UserService userService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object object) {
    final String username = request.getUserPrincipal().getName();
    if (rateLimitConfiguration.getNotAllowedClients().contains(username)) {
      RateLimitErrorHandler.handleForbiddenError(response,
          RateLimitHeader.X_RATE_LIMIT_USER_NOT_ALLOWED, username);
      return false;
    }
    final RateLimitData rateLimitData = getRateLimitData(request, response, username);
    if (Objects.isNull(rateLimitData)) {
      RateLimitErrorHandler.handleForbiddenError(response,
          RateLimitHeader.X_RATE_LIMIT_USER_NOT_ALLOWED, username);
      return false;
    }

    final ConsumptionProbe consumptionProbe = rateLimitData.getBucket()
        .tryConsumeAndReturnRemaining(1);
    if (consumptionProbe.isConsumed()) {
      response.addHeader(RateLimitHeader.X_RATE_LIMIT_REMAINING,
          String.valueOf(consumptionProbe.getRemainingTokens()));
      return true;
    }
    RateLimitErrorHandler.handleTooManyError(response, consumptionProbe);
    return false;
  }

  private RateLimitData getRateLimitData(HttpServletRequest request, HttpServletResponse response,
      String username) {
    RateLimitData rateLimitData = RateLimitStorage.getRateLimitData(username);
    if (Objects.isNull(rateLimitData)) {
      final User userByEmail = userService.getUserByEmail(request.getUserPrincipal().getName());
      if (Objects.isNull(userByEmail)) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return null;
      }
      final Bucket bucket = pricingPlanService.resolveBucketByUserPlan(userByEmail.getUserPlan(), request.getRequestURI());
      rateLimitData = new RateLimitData().setUser(userByEmail).setBucket(bucket);
      RateLimitStorage.addRateLimitData(username, rateLimitData);
    }
    return rateLimitData;
  }
}
