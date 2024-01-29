package com.codeless.api.automation.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = PageRequest.PageRequestBuilder.class)
public class PageRequest<T> {

  String nextToken;
  List<T> items;
}
