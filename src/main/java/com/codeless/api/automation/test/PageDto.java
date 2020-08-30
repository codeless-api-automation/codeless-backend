package com.codeless.api.automation.test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = PageDto.PageDtoBuilder.class)
public class PageDto<T> {

  Integer numberOfElements;
  Integer size;
  Integer number;
  Integer totalPages;
  List<T> items;
}
