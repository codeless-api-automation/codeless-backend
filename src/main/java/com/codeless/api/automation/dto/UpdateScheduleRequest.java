package com.codeless.api.automation.dto;

import com.codeless.api.automation.entity.enums.ScheduleState;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = UpdateScheduleRequest.UpdateScheduleRequestBuilder.class)
public class UpdateScheduleRequest {

  @NotEmpty
  @Size(min = 40, max = 40)
  String id;
  List<@Email String> emails;
  ScheduleState state;
}
