package com.codeless.api.automation.service;

import com.codeless.api.automation.dto.Page;
import com.codeless.api.automation.dto.Test;

public interface TestService {

  Test updateTest(Test testDto);
  Test saveTest(Test testDto);
  Page<Test> getAllTests(Integer page, Integer size);
  void deleteTest(Long id);
}
