package com.codeless.api.automation.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@RequiredArgsConstructor
public class NextTokenConverter {

  private final ObjectMapper objectMapper;

  public Map<String, AttributeValue> fromString(String nextToken) {
    if (Objects.isNull(nextToken)) {
      return null;
    }
    try {
      return objectMapper.readValue(
          decode(nextToken),
          new TypeReference<Map<String, AttributeValue>>() {});
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }
  }

  public String toString(Map<String, AttributeValue> nextToken) {
    if (Objects.isNull(nextToken)) {
      return null;
    }
    try {
      return encode(objectMapper.writeValueAsString(nextToken));
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }
  }

  private String decode(String nextToken) {
    byte[] decodedBytes = Base64.getDecoder().decode(nextToken);
    return new String(decodedBytes);
  }

  private String encode(String nextToken) {
    byte[] encodedBytes = Base64.getEncoder().encode(nextToken.getBytes());
    return new String(encodedBytes);
  }

}
