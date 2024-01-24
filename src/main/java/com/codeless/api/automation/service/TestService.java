package com.codeless.api.automation.service;

import com.codeless.api.automation.dto.Page;
import com.codeless.api.automation.dto.Test;
import java.security.Principal;

public interface TestService {

  Test updateTest(Test testDto, Principal principal);
  Test saveTest(Test testDto, Principal principal);
  Page<Test> getAllTests(Integer page, Integer size, Principal principal);
  void deleteTest(Long id, Principal principal);
}
