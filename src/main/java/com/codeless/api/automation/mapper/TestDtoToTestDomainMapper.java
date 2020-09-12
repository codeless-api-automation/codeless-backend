package com.codeless.api.automation.mapper;


import com.codeless.api.automation.domain.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TestDtoToTestDomainMapper implements Mapper<com.codeless.api.automation.dto.request.Test, Test> {

  private final ObjectMapper objectMapper;

  @Override
  public Test map(com.codeless.api.automation.dto.request.Test source) {
    return toTest(source.getJson());
  }

  private Test toTest(Map<Object, Object> testAttributes) {
    try {
      String testAttributesConvertedToString = objectMapper.writeValueAsString(testAttributes);
      return objectMapper.readValue(testAttributesConvertedToString, Test.class);
    } catch (Exception exception) {
      throw new RuntimeException();
    }
  }
}
