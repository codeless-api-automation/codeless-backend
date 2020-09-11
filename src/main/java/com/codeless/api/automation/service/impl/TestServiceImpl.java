package com.codeless.api.automation.service.impl;

import static java.util.stream.Collectors.toList;

import com.codeless.api.automation.dto.request.Test;
import com.codeless.api.automation.dto.response.Page;
import com.codeless.api.automation.mapper.Mapper;
import com.codeless.api.automation.repository.TestRepository;
import com.codeless.api.automation.service.TestService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TestServiceImpl implements TestService {

  private final Mapper<Test, com.codeless.api.automation.entity.Test> testDtoToTestMapper;
  private final Mapper<com.codeless.api.automation.entity.Test, Test> testToTestDtoMapper;
  private final TestRepository testRepository;

  @Override
  public Test saveTest(Test testDto) {
    return testToTestDtoMapper.map(testRepository.save(testDtoToTestMapper.map(testDto)));
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
}
