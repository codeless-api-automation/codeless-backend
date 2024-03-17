package com.codeless.api.automation.constant;

import com.codeless.api.automation.entity.enums.UserPlan;
import io.github.bucket4j.Bandwidth;
import java.time.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PricingPlan {

  DEFAULT("default", 20,40, 100),
  EXECUTIONS("/api/executions", 10,20, 50);

  private final String requestPath;
  private final int freeCapacity;
  private final int basicCapacity;
  private final int proCapacity;

  public Bandwidth getLimit(UserPlan userPlan){
    if (UserPlan.BASIC.equals(userPlan)) {
      return Bandwidth.builder().capacity(basicCapacity)
          .refillIntervally(basicCapacity, Duration.ofMinutes(50)).build();
    }
    if (UserPlan.PRO.equals(userPlan)) {
    return Bandwidth.builder().capacity(proCapacity)
        .refillIntervally(proCapacity, Duration.ofMinutes(30)).build();
    }
    return Bandwidth.builder().capacity(freeCapacity)
        .refillIntervally(freeCapacity, Duration.ofHours(1)).build();
  }

  public static PricingPlan resolvePricingFromPath(String requestPath) {
    for (PricingPlan pricingPlan : values()) {
      if (requestPath.contains(pricingPlan.getRequestPath())) {
        return pricingPlan;
      }
    }
    return PricingPlan.DEFAULT;
  }
}