package com.codeless.api.automation.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = ScheduleRequest.ScheduleRequestBuilder.class)
public class ScheduleRequest {

  @NotEmpty String scheduleName;
  @NotNull String testId;
  @NotNull Region region;
  @NotNull Timer timer;
  List<String> emails;
  String id;
}
