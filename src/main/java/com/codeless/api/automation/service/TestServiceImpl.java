package com.codeless.api.automation.service;

import static java.util.stream.Collectors.toList;

import com.codeless.api.automation.dto.response.Page;
import com.codeless.api.automation.dto.request.Test;
import com.codeless.api.automation.function.Mapper;
import com.codeless.api.automation.repository.TestRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

  @Autowired
  private Mapper<Test, com.codeless.api.automation.entity.Test> testDtoToTestMapper;
  @Autowired
  private Mapper<com.codeless.api.automation.entity.Test, Test> testToTestDtoMapper;
  @Autowired
  private TestRepository testRepository;

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
}
