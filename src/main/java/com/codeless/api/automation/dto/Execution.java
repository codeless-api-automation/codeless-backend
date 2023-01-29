package com.codeless.api.automation.dto;

import com.codeless.api.automation.entity.ExecutionStatus;
import com.codeless.api.automation.entity.ExecutionType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = Execution.ExecutionBuilder.class)
public class Execution {

  Long id;
  @NotNull ExecutionType type;
  @NotNull Region region;
  @NotEmpty String name;
  @NotNull Long testId;
  ExecutionStatus executionStatus;
}
