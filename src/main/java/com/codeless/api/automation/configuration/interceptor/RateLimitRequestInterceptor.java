package com.codeless.api.automation.configuration.interceptor;

import com.codeless.api.automation.constant.RateLimitHeader;
import com.codeless.api.automation.entity.User;
import com.codeless.api.automation.entity.enums.UserPlan;
import com.codeless.api.automation.service.PricingPlanService;
import com.codeless.api.automation.service.UserService;
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

  private final PricingPlanService pricingPlanService;
  private final UserService userService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object object) {

    final User userByEmail = userService.getUserByEmail(request.getUserPrincipal().getName());
        if (Objects.isNull(userByEmail)) {
      response.setStatus(HttpStatus.BAD_REQUEST.value());
      return false;
    }

    final Bucket tokenBucket = pricingPlanService.resolveBucketByUserPlan(UserPlan.FREE);
    final ConsumptionProbe consumptionProbe = tokenBucket.tryConsumeAndReturnRemaining(1);
    if (consumptionProbe.isConsumed()) {
      response.addHeader(RateLimitHeader.X_RATE_LIMIT_REMAINING, String.valueOf(consumptionProbe.getRemainingTokens()));
      return true;
    }
      TooManyRequestErrorHandler.handleTooManyError(response, consumptionProbe);
      return false;
    }
}
