package com.codeless.api.automation.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = Schedule.ScheduleBuilder.class)
public class Schedule {

  @NotEmpty String scheduleName;
  @NotEmpty List<Test> tests;
  @NotNull Region region;
  @NotNull Timer timer;
  List<String> emails;
  Long id;
}
