package com.codeless.api.automation.service;

import com.codeless.api.automation.dto.response.Page;
import com.codeless.api.automation.dto.request.Test;

public interface TestService {

  Test saveTest(Test testDto);
  Page<Test> getAllTests(Integer page, Integer size);
}
