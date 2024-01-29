package com.codeless.api.automation.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LogsConverter {

  private final ObjectMapper objectMapper;

  public List<String> fromString(String logs) {
    if (Objects.isNull(logs)) {
      return null;
    }
    try {
      return objectMapper.readValue(logs, new TypeReference<List<String>>() {
      });
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }
  }

  public String toString(List<String> logs) {
    if (Objects.isNull(logs)) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(logs);
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }
  }
}
