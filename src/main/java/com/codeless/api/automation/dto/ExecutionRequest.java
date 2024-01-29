package com.codeless.api.automation.dto;

import com.codeless.api.automation.entity.enums.ExecutionStatus;
import com.codeless.api.automation.entity.enums.ExecutionType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = ExecutionRequest.ExecutionRequestBuilder.class)
public class ExecutionRequest {

  String id;
  @NotNull ExecutionType type;
  @NotNull Region region;
  @NotEmpty String name;
  @NotNull String testId;
  ExecutionStatus executionStatus;
}
