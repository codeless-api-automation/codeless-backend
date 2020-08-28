package com.codeless.api.automation.test;

import com.codeless.api.automation.function.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

  @Autowired
  private Mapper<TestDto, Test> testMapper;
  @Autowired
  private TestRepository testRepository;

  @Override
  public Test saveTest(TestDto testDto) {
    return testRepository.save(testMapper.map(testDto));
  }
}
