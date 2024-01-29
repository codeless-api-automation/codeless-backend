package com.codeless.api.automation.service;

import com.codeless.api.automation.dto.PageRequest;
import com.codeless.api.automation.dto.TestRequest;

public interface TestService {

  void updateTest(TestRequest testRequest, String customerId);
  void createTest(TestRequest testRequest, String customerId);
  PageRequest<TestRequest> getAllTests(Integer maxResults, String nextToken, String customerId);
  void deleteTest(String id, String customerId);
}
