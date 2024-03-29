package com.codeless.api.automation.service;

import com.codeless.api.automation.entity.enums.UserPlan;
import io.github.bucket4j.Bucket;

public interface PricingPlanService {
  Bucket resolveBucketByUserPlan(UserPlan userPlan);
  Bucket resolveBucketByIp(String ipAddress);
}