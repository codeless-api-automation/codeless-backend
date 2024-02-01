package com.codeless.api.automation.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = ScheduleRequest.ScheduleRequestBuilder.class)
public class ScheduleRequest {

  @NotEmpty
  @Size(max = 64)
  String scheduleName;
  @NotNull
  @Size(min = 40, max = 40)
  String testId;
  @NotNull Region region;
  @NotNull Timer timer;
  List<String> emails;
  String id;
}
