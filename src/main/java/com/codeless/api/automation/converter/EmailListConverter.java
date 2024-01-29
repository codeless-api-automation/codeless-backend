package com.codeless.api.automation.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailListConverter {

  private final ObjectMapper objectMapper;

  public List<String> fromString(String emails) {
    if (Objects.isNull(emails)) {
      return null;
    }
    try {
      return objectMapper.readValue(emails, new TypeReference<List<String>>() {
      });
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }
  }

  public String toString(List<String> emails) {
    if (Objects.isNull(emails)) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(emails);
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }
  }
}
