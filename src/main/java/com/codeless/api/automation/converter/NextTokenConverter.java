package com.codeless.api.automation.converter;

import com.codeless.api.automation.dto.NextToken;
import com.codeless.api.automation.exception.ApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Base64;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@RequiredArgsConstructor
public class NextTokenConverter {

  private final ObjectMapper objectMapper;

  public NextToken fromString(String nextToken) {
    if (Objects.isNull(nextToken)) {
      return null;
    }
    try {
      return objectMapper.readValue(decode(nextToken), new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      log.info("Invalid next token!", e);
      throw new ApiException("Invalid next token!", HttpStatus.BAD_REQUEST.value());
    }
  }

  public String toString(NextToken nextToken) {
    if (Objects.isNull(nextToken)) {
      return null;
    }
    try {
      return encode(objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
          .writeValueAsString(nextToken));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
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
