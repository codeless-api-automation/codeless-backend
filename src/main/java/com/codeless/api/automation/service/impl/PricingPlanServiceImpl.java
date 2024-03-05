package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.constant.PricingPlan;
import com.codeless.api.automation.entity.enums.UserPlan;
import com.codeless.api.automation.service.PricingPlanService;
import io.github.bucket4j.Bucket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class PricingPlanServiceImpl implements PricingPlanService {

    private final Map<UserPlan, Bucket> PLAN_BUCKETS = new ConcurrentHashMap<>();
    private final Map<String, Bucket> IP_BUCKETS = new ConcurrentHashMap<>();

    @Override
    public Bucket resolveBucketByUserPlan(UserPlan userPlan, String requestPath) {
        return PLAN_BUCKETS.computeIfAbsent(userPlan, newUserPlan -> newBucket(newUserPlan, requestPath));
    }

    @Override
    public Bucket resolveBucketByIp(String ipAddress, String requestPath) {
        return IP_BUCKETS.computeIfAbsent(ipAddress, userPlan -> newBucket(UserPlan.FREE, requestPath));
    }

    private Bucket newBucket(UserPlan userPlan, String requestPath) {
        final PricingPlan pricingPlan = PricingPlan.resolvePricingFromPath(requestPath);
        return Bucket.builder()
            .addLimit(pricingPlan.getLimit(userPlan))
            .build();
    }
}