package com.codeless.api.automation.controller;

import static com.codeless.api.automation.util.RestApiConstant.SINGLE_TEST_RESOURCE_WITH_ROOT_PATH;
import static com.codeless.api.automation.util.RestApiConstant.TEST_RESOURCE;

import com.codeless.api.automation.dto.Page;
import com.codeless.api.automation.dto.Test;
import com.codeless.api.automation.service.TestService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(TEST_RESOURCE)
@Validated
@RequiredArgsConstructor
public class TestController {

  private final TestService testService;

  @PutMapping
  public Test updateTest(@RequestBody @Valid Test test) {
    return testService.updateTest(test);
  }

  @PostMapping
  public ResponseEntity<Test> createTest(@RequestBody @Valid Test test) {
    Test createdTest = testService.saveTest(test);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .location(URI.create(SINGLE_TEST_RESOURCE_WITH_ROOT_PATH + createdTest.getId()))
        .body(createdTest);
  }

  @GetMapping
  public Page<Test> getAllTests(@RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "5") Integer size) {
    return testService.getAllTests(page, size);
  }

  @DeleteMapping(path = "/{testId}")
  public void deleteTest(@PathVariable Long testId) {
    testService.deleteTest(testId);
  }

}
