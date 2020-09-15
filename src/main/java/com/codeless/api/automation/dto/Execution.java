package com.codeless.api.automation.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = Execution.ExecutionBuilder.class)
public class Execution {

  Long executionId;
  @NotNull Region region;
  @NotEmpty List<Test> tests;
}
