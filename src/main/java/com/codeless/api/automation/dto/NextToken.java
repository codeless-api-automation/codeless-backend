package com.codeless.api.automation.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = NextToken.NextTokenBuilder.class)
public class NextToken {

  String created;
  String id;

}
