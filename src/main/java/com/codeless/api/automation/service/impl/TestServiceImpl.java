package com.codeless.api.automation.service.impl;

import static java.util.stream.Collectors.toList;

import com.codeless.api.automation.dto.Page;
import com.codeless.api.automation.dto.Test;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.mapper.Mapper;
import com.codeless.api.automation.repository.TestRepository;
import com.codeless.api.automation.service.TestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

  private final Mapper<Test, com.codeless.api.automation.entity.Test> testDtoToTestMapper;
  private final Mapper<com.codeless.api.automation.entity.Test, Test> testToTestDtoMapper;
  private final TestRepository testRepository;
  private final ObjectMapper objectMapper;

  @Override
  public Test updateTest(Test testDto) {
    Long testId = testDto.getId();
    if (testId != null) {
      com.codeless.api.automation.entity.Test existingTest = testRepository.findById(testId)
          .orElseThrow(
              () -> new ApiException("The test was not found!", HttpStatus.BAD_REQUEST.value()));
      existingTest.setName(testDto.getName());
      existingTest.setJson(toString(testDto.getJson()));
      return testToTestDtoMapper.map(testRepository.save(existingTest));
    }
    throw new ApiException("The test is not created yet to be edited!",
        HttpStatus.BAD_REQUEST.value());
  }

  @Override
  public Test saveTest(Test testDto) {
    if (testDto.getId() == null) {
      return testToTestDtoMapper.map(testRepository.save(testDtoToTestMapper.map(testDto)));
    }
    throw new ApiException("The id is not required!", HttpStatus.BAD_REQUEST.value());
  }

  @Override
  public Page<Test> getAllTests(Integer page, Integer size) {
    org.springframework.data.domain.Page<com.codeless.api.automation.entity.Test> tests =
        testRepository.findAll(PageRequest.of(page, size));
    List<Test> dtoTests = tests
        .getContent()
        .stream()
        .map(testToTestDtoMapper::map)
        .collect(toList());
    return Page.<Test>builder()
        .size(tests.getSize())
        .number(tests.getNumber())
        .totalPages(tests.getTotalPages())
        .totalElements(tests.getTotalElements())
        .numberOfElements(tests.getNumberOfElements())
        .items(dtoTests)
        .build();
  }

  @Override
  public void deleteTests(List<Test> tests) {
    testRepository.deleteAll(tests.stream()
        .map(testDtoToTestMapper::map)
        .collect(Collectors.toList()));
  }

  private String toString(Map<Object, Object> json) {
    try {
      return objectMapper.writeValueAsString(json);
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }
  }
}
