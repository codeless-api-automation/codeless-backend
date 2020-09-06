package com.codeless.api.automation.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = Page.PageBuilder.class)
public class Page<T> {

  Integer numberOfElements;
  Integer size;
  Integer number;
  Integer totalPages;
  Long totalElements;
  List<T> items;
}
