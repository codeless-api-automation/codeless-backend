package com.codeless.api.automation.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = Profile.ProfileBuilder.class)
public class Profile {

  Integer usedTestsCount;
  Integer allowedTestsCount;
  Integer usedSchedulesCount;
  Integer allowedSchedulesCount;
}
