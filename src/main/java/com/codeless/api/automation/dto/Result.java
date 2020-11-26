package com.codeless.api.automation.dto;

import com.codeless.api.automation.entity.TestStatus;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = Result.ResultBuilder.class)
public class Result {

  List<String> logs;
  TestStatus testStatus;
}
