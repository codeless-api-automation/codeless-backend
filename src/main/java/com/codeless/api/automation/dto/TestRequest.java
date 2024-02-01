package com.codeless.api.automation.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = TestRequest.TestRequestBuilder.class)
public class TestRequest {

  String id;
  @NotBlank
  @Size(max = 64)
  String name;
  @NotNull List<Map<Object, Object>> json;
}
