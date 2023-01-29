package com.codeless.api.automation.mapper;

import com.codeless.api.automation.dto.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestToTestDtoMapper implements Mapper<com.codeless.api.automation.entity.Test, Test> {

  private final ObjectMapper objectMapper;

  @Override
  public Test map(com.codeless.api.automation.entity.Test source) {
    return Test.builder()
        .id(source.getId())
        .name(source.getName())
        .json(toMap(source.getJson()))
        .build();
  }

  private List<Map<Object, Object>> toMap(String json) {
    try {
      return objectMapper.readValue(json, new TypeReference<List<Map<Object, Object>>>() {
      });
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }
  }
}
