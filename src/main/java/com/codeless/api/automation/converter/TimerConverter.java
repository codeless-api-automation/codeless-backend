package com.codeless.api.automation.converter;

import com.codeless.api.automation.dto.Timer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TimerConverter {

  private final ObjectMapper objectMapper;

  public Timer fromString(String timer) {
    if (Objects.isNull(timer)) {
      return null;
    }
    try {
      return objectMapper.readValue(timer, new TypeReference<Timer>() {
      });
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }
  }

  public String toString(Timer timer) {
    if (Objects.isNull(timer)) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(timer);
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }
  }
}
