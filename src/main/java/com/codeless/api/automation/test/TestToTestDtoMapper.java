package com.codeless.api.automation.test;

import com.codeless.api.automation.function.Mapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestToTestDtoMapper implements Mapper<Test, TestDto> {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public TestDto map(Test source) {
    return TestDto.builder()
        .id(source.getId())
        .name(source.getName())
        .json(toMap(source.getJson()))
        .build();
  }

  private Map<Object, Object> toMap(String json) {
    try {
      return objectMapper.readValue(json, new TypeReference<HashMap<Object, Object>>() {
      });
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }
  }
}
