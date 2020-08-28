package com.codeless.api.automation.test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = TestDto.TestRequestBuilder.class)
public class TestDto {

  @NotBlank String name;
  @NotNull Map<Object, Object> json;
}
