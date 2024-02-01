package com.codeless.api.automation.dto;

import com.codeless.api.automation.entity.enums.ExecutionStatus;
import com.codeless.api.automation.entity.enums.ExecutionType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.Instant;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = ExecutionRequest.ExecutionRequestBuilder.class)
public class ExecutionRequest {

  String id;
  @NotNull ExecutionType type;
  @NotNull Region region;
  @NotNull
  @Size(min = 40, max = 40)
  String testId;
  String name;
  ExecutionStatus executionStatus;
  Instant submitted;
}
