package com.codeless.api.automation.service.impl;

import static java.util.stream.Collectors.toList;

import com.codeless.api.automation.service.UsernameStorageService;
import com.codeless.api.automation.dto.Page;
import com.codeless.api.automation.dto.Test;
import com.codeless.api.automation.entity.Execution;
import com.codeless.api.automation.entity.ExecutionStatus;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.mapper.Mapper;
import com.codeless.api.automation.repository.ExecutionRepository;
import com.codeless.api.automation.repository.ScheduleRepository;
import com.codeless.api.automation.repository.TestRepository;
import com.codeless.api.automation.service.TestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

  private final Mapper<Test, com.codeless.api.automation.entity.Test> testDtoToTestMapper;
  private final Mapper<com.codeless.api.automation.entity.Test, Test> testToTestDtoMapper;
  private final TestRepository testRepository;
  private final ScheduleRepository scheduleRepository;
  private final ExecutionRepository executionRepository;
  private final UsernameStorageService userStorageService;
  private final ObjectMapper objectMapper;

  @Override
  public Test updateTest(Test testDto) {
    Long testId = testDto.getId();
    if (testId != null) {
      com.codeless.api.automation.entity.Test existingTest = testRepository.findByIdAndUsername(
              testId, getUserName())
          .orElseThrow(
              () -> new ApiException("The test was not found!", HttpStatus.BAD_REQUEST.value()));
      existingTest.setName(testDto.getName());
      existingTest.setJson(toString(testDto.getJson()));
      return testToTestDtoMapper.map(testRepository.save(existingTest));
    }
    throw new ApiException("The test is not created yet to be edited!",
        HttpStatus.BAD_REQUEST.value());
  }

  @Override
  public Test saveTest(Test testDto) {
    if (testDto.getId() == null) {
      return testToTestDtoMapper.map(testRepository.save(testDtoToTestMapper.map(testDto)));
    }
    throw new ApiException("The id is not required!", HttpStatus.BAD_REQUEST.value());
  }

  @Override
  public Page<Test> getAllTests(Integer page, Integer size) {
    org.springframework.data.domain.Page<com.codeless.api.automation.entity.Test> tests =
        testRepository.findAllByUsername(getUserName(), PageRequest.of(page, size));
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

  @Override
  public void deleteTest(Long id) {
    List<com.codeless.api.automation.entity.Schedule> schedules =
        scheduleRepository.findAllByTestIdAndUsername(id, getUserName());
    if (!schedules.isEmpty()) {
      throw new ApiException("There are schedules associated with this test. "
          + "Please delete schedules first before deleting the test.",
          HttpStatus.BAD_REQUEST.value());
    }

    List<Execution> executions = executionRepository.findAllByTestIdAndUsernameAndStatusIn(id,
        getUserName(),
        Arrays.asList(ExecutionStatus.STARTED, ExecutionStatus.PENDING));
    if (!executions.isEmpty()) {
      throw new ApiException("Executions are in progress. "
          + "Please try to delete this test later.", HttpStatus.BAD_REQUEST.value());
    }

    testRepository.deleteByIdAndUsername(id, getUserName());
  }

  private String toString(List<Map<Object, Object>> json) {
    try {
      return objectMapper.writeValueAsString(json);
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }
  }

  private String getUserName() {
    return userStorageService.getUsername(
        Objects.requireNonNull(RequestContextHolder.getRequestAttributes()).getSessionId());
  }
}
