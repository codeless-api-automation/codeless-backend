package com.codeless.api.automation.mapper;

import com.codeless.api.automation.dto.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

@Service
@RequiredArgsConstructor
public class TestDtoToTestMapper implements Mapper<Test, com.codeless.api.automation.entity.Test> {

  private final ObjectMapper objectMapper;

  @Override
  public com.codeless.api.automation.entity.Test map(Test source) {
    com.codeless.api.automation.entity.Test test = new com.codeless.api.automation.entity.Test();
    test.setId(source.getId());
    test.setName(source.getName());
    test.setJson(toString(source.getJson()));
    return test;
  }

  private String toString(List<Map<Object, Object>> json) {
    try {
      return objectMapper.writeValueAsString(json);
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }
  }
}
