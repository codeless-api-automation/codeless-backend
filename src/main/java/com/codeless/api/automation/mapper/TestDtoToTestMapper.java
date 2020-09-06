package com.codeless.api.automation.mapper;

import com.codeless.api.automation.dto.request.Test;
import com.codeless.api.automation.function.Mapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDtoToTestMapper implements Mapper<Test, com.codeless.api.automation.entity.Test> {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public com.codeless.api.automation.entity.Test map(Test source) {
    com.codeless.api.automation.entity.Test test = new com.codeless.api.automation.entity.Test();
    test.setName(source.getName());
    test.setJson(toString(source.getJson()));
    return test;
  }

  private String toString(Map<Object, Object> json) {
    try {
      return objectMapper.writeValueAsString(json);
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }
  }
}
