package com.codeless.api.automation.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = Execution.ExecutionBuilder.class)
public class Execution {

}
