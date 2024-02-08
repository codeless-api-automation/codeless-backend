package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.converter.NextTokenConverter;
import com.codeless.api.automation.converter.TestConverter;
import com.codeless.api.automation.dto.NextToken;
import com.codeless.api.automation.dto.PageRequest;
import com.codeless.api.automation.dto.TestRequest;
import com.codeless.api.automation.entity.Schedule;
import com.codeless.api.automation.entity.Test;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.repository.ScheduleRepository;
import com.codeless.api.automation.repository.TestRepository;
import com.codeless.api.automation.service.TestService;
import com.codeless.api.automation.util.ApiValidationUtil;
import com.codeless.api.automation.util.PersistenceUtil;
import com.codeless.api.automation.util.RandomIdGenerator;
import com.codeless.api.automation.util.RestApiConstant;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

  private final TestRepository testRepository;
  private final ScheduleRepository scheduleRepository;
  private final TestConverter testConverter;
  private final NextTokenConverter nextTokenConverter;

  @Override
  public void updateTest(TestRequest testRequest, String customerId) {
    String testId = testRequest.getId();
    if (testId == null) {
      throw new ApiException("The test is not created yet to be edited!",
          HttpStatus.BAD_REQUEST.value());
    }
    Test existingTest = testRepository.get(testId);
    if (existingTest == null) {
      throw new ApiException("The test was not found!", HttpStatus.BAD_REQUEST.value());
    }
    if (!existingTest.getCustomerId().equals(customerId)) {
      throw new ApiException("Unauthorized to access!", HttpStatus.UNAUTHORIZED.value());
    }
    existingTest.setName(testRequest.getName());
    existingTest.setJson(testConverter.toString(testRequest.getJson()));
    existingTest.setLastModified(Instant.now());
    testRepository.put(existingTest);
  }

  @Override
  public void createTest(TestRequest testRequest, String customerId) {
    Test test = new Test();
    test.setId(RandomIdGenerator.generateTestId());
    test.setName(testRequest.getName());
    test.setJson(testConverter.toString(testRequest.getJson()));
    test.setCustomerId(customerId);
    test.setCreated(Instant.now());
    test.setLastModified(Instant.now());
    testRepository.create(test);
  }

  @Override
  public PageRequest<TestRequest> getAllTests(
      Integer maxResults,
      String nextTokenAsString,
      String customerId) {
    NextToken nextToken = nextTokenConverter.fromString(nextTokenAsString);
    ApiValidationUtil.validateNextTokenOwnership(nextToken, customerId);
    ApiValidationUtil.validateNextTokenForRequestByCustomerId(nextToken);

    Page<Test> tests = testRepository.listTestsByCustomerId(
        customerId,
        PersistenceUtil.buildLastEvaluatedKeyForRequestByCustomerId(nextToken),
        maxResults);
    List<TestRequest> items = tests.items().stream()
        .map(test -> TestRequest.builder()
            .id(test.getId())
            .name(test.getName())
            .json(testConverter.fromString(test.getJson()))
            .build())
        .collect(Collectors.toList());

    return PageRequest.<TestRequest>builder()
        .nextToken(nextTokenConverter.toString(
            PersistenceUtil.buildNextTokenForRequestByCustomerId(tests.lastEvaluatedKey())))
        .items(items)
        .build();
  }

  @Override
  public void deleteTest(String testId, String customerId) {
    Test existingTest = testRepository.get(testId);
    if (existingTest == null) {
      throw new ApiException("The test was not found!", HttpStatus.BAD_REQUEST.value());
    }
    if (!existingTest.getCustomerId().equals(customerId)) {
      throw new ApiException(RestApiConstant.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED.value());
    }
    Page<Schedule> schedules =
        scheduleRepository.listSchedulesByTestId(testId, null, 1);
    if (!schedules.items().isEmpty()) {
      throw new ApiException("There are schedules associated with this test. "
          + "Please delete schedules first before deleting the test.",
          HttpStatus.BAD_REQUEST.value());
    }
    testRepository.delete(testId);
  }

}
