package com.codeless.api.automation.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = TestRequest.TestRequestBuilder.class)
public class TestRequest {

  String id;
  @NotBlank String name;
  @NotNull List<Map<Object, Object>> json;
}
