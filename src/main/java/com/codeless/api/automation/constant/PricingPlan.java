package com.codeless.api.automation.constant;

import com.codeless.api.automation.entity.enums.UserPlan;
import io.github.bucket4j.Bandwidth;
import java.time.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PricingPlan {

  FREE(20),
  BASIC(40),
  PROFESSIONAL(100);

  private final int bucketCapacity;

  public Bandwidth getLimit() {
    return Bandwidth.builder().capacity(bucketCapacity)
        .refillIntervally(bucketCapacity, Duration.ofHours(1)).build();
  }

  public static PricingPlan resolvePricingFromUserPlan(UserPlan userPlan) {
    if (UserPlan.BASIC.equals(userPlan)) {
      return BASIC;
    }
    if (UserPlan.PRO.equals(userPlan)) {
      return PROFESSIONAL;
    }
    return FREE;
  }
}