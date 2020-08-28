package com.codeless.api.automation.test;

import com.codeless.api.automation.function.Mapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestMapper implements Mapper<TestDto, Test> {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public Test map(TestDto source) {
    Test test = new Test();
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
