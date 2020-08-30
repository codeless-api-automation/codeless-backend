package com.codeless.api.automation.test;

public interface TestService {

  TestDto saveTest(TestDto testDto);
  PageDto<TestDto> getAllTests(Integer page, Integer size);
}
