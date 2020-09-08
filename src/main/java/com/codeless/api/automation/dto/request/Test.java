package com.codeless.api.automation.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = Test.TestBuilder.class)
public class Test {

  Long id;
  @NotBlank String name;
  @NotNull Map<Object, Object> json;
}
