package com.codeless.api.automation.controller;

import static com.codeless.api.automation.util.RestApiConstant.SINGLE_TEST_RESOURCE_WITH_ROOT_PATH;
import static com.codeless.api.automation.util.RestApiConstant.TEST_RESOURCE;

import com.codeless.api.automation.dto.request.Test;
import com.codeless.api.automation.dto.response.Page;
import com.codeless.api.automation.service.TestService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(TEST_RESOURCE)
@Validated
@AllArgsConstructor
public class TestController {

  private final TestService testService;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Test> createTest(@RequestBody @Valid Test test) {
    Test createdTest = testService.saveTest(test);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .location(URI.create(SINGLE_TEST_RESOURCE_WITH_ROOT_PATH + createdTest.getId()))
        .body(createdTest);
  }

  @RequestMapping(method = RequestMethod.GET)
  public Page<Test> getAllTests(@RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "5") Integer size) {
    return testService.getAllTests(page, size);
  }

  @RequestMapping(method = RequestMethod.DELETE)
  public void deleteTests(@RequestBody @Valid @NotEmpty List<Test> tests) {
    testService.deleteTests(tests);
  }

}
