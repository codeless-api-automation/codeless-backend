package com.codeless.api.automation.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestConverter {

  private final ObjectMapper objectMapper;

  public List<Map<Object, Object>> fromString(String json) {
    try {
      return objectMapper.readValue(json, new TypeReference<List<Map<Object, Object>>>() {
      });
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }
  }

  public String toString(List<Map<Object, Object>> json) {
    try {
      return objectMapper.writeValueAsString(json);
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }
  }
}
