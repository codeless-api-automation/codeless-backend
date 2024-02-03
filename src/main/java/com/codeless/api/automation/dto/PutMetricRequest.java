package com.codeless.api.automation.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = PutMetricRequest.PutMetricRequestBuilder.class)
public class PutMetricRequest {

  @NotNull
  Long timestamp;
  @NotNull
  Double value;
  Map<String, String> labels;
}
