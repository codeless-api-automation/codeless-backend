package com.codeless.api.automation.dto;

import com.codeless.api.automation.entity.enums.ScheduleState;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = ScheduleRequest.ScheduleRequestBuilder.class)
public class ScheduleRequest {

  String id;
  @NotEmpty
  @Size(min = 4, max = 128, message = "The schedule name '${validatedValue}' must be between {min} and {max} characters long.")
  String scheduleName;
  @NotNull
  @Size(min = 40, max = 40)
  String testId;
  @NotNull Region region;
  @NotNull Timer timer;
  @Size(min=1, max=5)
  List<@Email String> emails;
  ScheduleState state;
}
