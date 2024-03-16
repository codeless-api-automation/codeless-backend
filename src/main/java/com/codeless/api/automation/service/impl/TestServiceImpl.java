package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.appconfig.LimitsConfigProvider;
import com.codeless.api.automation.converter.NextTokenConverter;
import com.codeless.api.automation.converter.TestConverter;
import com.codeless.api.automation.dto.NextToken;
import com.codeless.api.automation.dto.PageRequest;
import com.codeless.api.automation.dto.TestRequest;
import com.codeless.api.automation.entity.Schedule;
import com.codeless.api.automation.entity.Test;
import com.codeless.api.automation.entity.User;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.repository.ScheduleRepository;
import com.codeless.api.automation.repository.TestRepository;
import com.codeless.api.automation.repository.UserRepository;
import com.codeless.api.automation.service.TestService;
import com.codeless.api.automation.util.ApiValidationUtil;
import com.codeless.api.automation.util.PersistenceUtil;
import com.codeless.api.automation.util.RandomIdGenerator;
import com.codeless.api.automation.util.RestApiConstant;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
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
  private final UserRepository userRepository;
  private final LimitsConfigProvider limitsConfigProvider;

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
    User user = userRepository.get(customerId);

    Integer allowedTestsLimit =
        limitsConfigProvider.getTestsLimit(user.getUserPlan().getValue(), customerId);
    if (Objects.nonNull(user.getTestsCounter()) && user.getTestsCounter() > allowedTestsLimit) {
      throw new ApiException("You exceeded number of tests allowed by your plan. "
          + "Please consider upgrading your plan.", HttpStatus.BAD_REQUEST.value());
    }

    Test test = new Test();
    test.setId(RandomIdGenerator.generateTestId());
    test.setName(testRequest.getName());
    test.setJson(testConverter.toString(testRequest.getJson()));
    test.setCustomerId(customerId);
    test.setCreated(Instant.now());
    test.setLastModified(Instant.now());
    testRepository.create(test);

    user.setTestsCounter(user.getTestsCounter() == null ? 1 : user.getTestsCounter() + 1);
    userRepository.put(user);
  }

  @Override
  public PageRequest<TestRequest> getAllTests(
      Integer maxResults,
      String nextTokenAsString,
      String customerId) {
    NextToken nextToken = nextTokenConverter.fromString(nextTokenAsString);
    ApiValidationUtil.validateNextTokenInListByCustomerId(nextToken);

    Page<Test> tests = testRepository.listTestsByCustomerId(
        customerId,
        PersistenceUtil.buildLastEvaluatedKeyInListByCustomerId(nextToken, customerId),
        maxResults);
    List<TestRequest> items = tests.items().stream()
        .map(test -> TestRequest.builder()
            .id(test.getId())
            .name(test.getName())
            .build())
        .collect(Collectors.toList());

    return PageRequest.<TestRequest>builder()
        .nextToken(nextTokenConverter.toString(
            PersistenceUtil.buildNextTokenInListByCustomerId(tests.lastEvaluatedKey())))
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

    User user = userRepository.get(customerId);
    user.setTestsCounter(Objects.isNull(user.getTestsCounter()) ? 0 : user.getTestsCounter() - 1);
    userRepository.put(user);
  }

  @Override
  public TestRequest getTest(String testId, String customerId) {
    Test existingTest = testRepository.get(testId);
    if (existingTest == null) {
      throw new ApiException("The test was not found!", HttpStatus.BAD_REQUEST.value());
    }
    if (!existingTest.getCustomerId().equals(customerId)) {
      throw new ApiException(RestApiConstant.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED.value());
    }
    return TestRequest.builder()
        .id(existingTest.getId())
        .name(existingTest.getName())
        .json(testConverter.fromString(existingTest.getJson()))
        .build();
  }



}
