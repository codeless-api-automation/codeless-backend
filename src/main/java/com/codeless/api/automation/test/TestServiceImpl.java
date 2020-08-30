package com.codeless.api.automation.test;

import com.codeless.api.automation.function.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

  @Autowired
  private Mapper<TestDto, Test> testDtoToTestMapper;
  @Autowired
  private Mapper<Test, TestDto> testToTestDtoMapper;
  @Autowired
  private TestRepository testRepository;

  @Override
  public TestDto saveTest(TestDto testDto) {
    return testToTestDtoMapper.map(testRepository.save(testDtoToTestMapper.map(testDto)));
  }
}
