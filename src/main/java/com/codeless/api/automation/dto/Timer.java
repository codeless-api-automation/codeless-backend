package com.codeless.api.automation.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = Timer.TimerBuilder.class)
public class Timer {

  @NotEmpty
  String type;
  String minute;
  String hour;
  String week;
  String time;
}
