package com.codeless.api.automation.appconfig;

import com.codeless.api.automation.appconfig.LimitsConfig.Limit;
import com.codeless.api.automation.appconfig.LimitsConfig.LimitData;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;

@RequiredArgsConstructor
public class LimitsConfigProvider {

  private static final String NUMBER_OF_TESTS_LIMIT_NAME = "number-of-tests-limit";
  private static final String NUMBER_OF_SCHEDULES_LIMIT_NAME = "number-of-schedules-limit";
  private static final String NUMBER_OF_REQUESTS_IN_TEST_LIMIT_NAME = "number-of-requests-in-test-limit";
  private static final String TEST_IN_BYTES_LIMIT_NAME = "test-in-bytes-limit";

  private static final Integer DEFAULT_LIMIT_VALUE = 0;

  private final ResourceLoader resourceLoader;
  private final ObjectMapper objectMapper;

  public LimitsConfig getLimitsConfig() {
    try {
      Resource resource = resourceLoader.getResource("classpath:config/service-limits.json");
      byte[] countriesInBinary = FileCopyUtils.copyToByteArray(resource.getInputStream());
      return objectMapper.readValue(new String(countriesInBinary), LimitsConfig.class);
    } catch (Exception exp) {
      throw new RuntimeException(exp);
    }
  }

  public Integer getNumberOfTestsLimit(String plan, String customerId) {
    return getLimit(NUMBER_OF_TESTS_LIMIT_NAME, plan, customerId);
  }

  public Integer getNumberOfSchedulesLimit(String plan, String customerId) {
    return getLimit(NUMBER_OF_SCHEDULES_LIMIT_NAME, plan, customerId);
  }

  public Integer getNumberOfRequestsInTestLimit(String plan, String customerId) {
    return getLimit(NUMBER_OF_REQUESTS_IN_TEST_LIMIT_NAME, plan, customerId);
  }

  public Integer getTestInBytesLimit(String plan, String customerId) {
    return getLimit(TEST_IN_BYTES_LIMIT_NAME, plan, customerId);
  }

  private Integer getLimit(String limitName, String plan, String customerId) {
    LimitsConfig limitsConfig = getLimitsConfig();
    if (limitsConfig == null) {
      return DEFAULT_LIMIT_VALUE;
    }

    Map<String, Limit> limitByName = limitsConfig.getLimitByName();
    if (Objects.isNull(limitByName) || CollectionUtils.isEmpty(limitByName)) {
      return DEFAULT_LIMIT_VALUE;
    }

    Limit limit = limitByName.get(limitName);
    if (Objects.isNull(limit)) {
      return DEFAULT_LIMIT_VALUE;
    }

    LimitData limitDetail = limit.getLimitByPlan().get(plan);
    if (Objects.isNull(limitDetail)) {
      return DEFAULT_LIMIT_VALUE;
    }

    Map<String, Integer> limitByCustomerId = limitDetail.getOverride();
    if (Objects.isNull(limitByCustomerId)) {
      return DEFAULT_LIMIT_VALUE;
    }
    return limitByCustomerId.getOrDefault(customerId, limitDetail.getDefaultLimit());
  }
}
