package com.codeless.api.automation.mapper;

import com.codeless.api.automation.entity.Result;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

@Service
@RequiredArgsConstructor
public class ResultMapper implements Mapper<Result, com.codeless.api.automation.dto.Result> {

  private final ObjectMapper objectMapper;

  @Override
  @SneakyThrows
  public com.codeless.api.automation.dto.Result map(Result source) {
    return com.codeless.api.automation.dto.Result.builder()
        .logs(objectMapper.readValue(source.getLogs(), new TypeReference<List<String>>() {
        }))
        .testStatus(source.getStatus())
        .build();
  }
}
