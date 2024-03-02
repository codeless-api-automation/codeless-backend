package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.appconfig.LimitsConfigProvider;
import com.codeless.api.automation.dto.Profile;
import com.codeless.api.automation.entity.User;
import com.codeless.api.automation.repository.UserRepository;
import com.codeless.api.automation.service.ProfileService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

  private final UserRepository userRepository;
  private final LimitsConfigProvider limitsConfigProvider;

  @Override
  public Profile getProfile(String customerId) {
    User user = userRepository.get(customerId);
    return Profile.builder()
        .usedTestsCount(Objects.isNull(user.getTestsCounter()) ? 0 : user.getTestsCounter())
        .allowedTestsCount(
            limitsConfigProvider.getTestsLimit(user.getUserPlan().getValue(), customerId))
        .usedSchedulesCount(
            Objects.isNull(user.getSchedulesCounter()) ? 0 : user.getSchedulesCounter())
        .allowedSchedulesCount(
            limitsConfigProvider.getSchedulesLimit(user.getUserPlan().getValue(), customerId))
        .build();
  }
}
