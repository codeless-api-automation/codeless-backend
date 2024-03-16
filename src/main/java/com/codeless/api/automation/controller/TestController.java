package com.codeless.api.automation.controller;

import static com.codeless.api.automation.util.RestApiConstant.TEST_RESOURCE;

import com.codeless.api.automation.dto.PageRequest;
import com.codeless.api.automation.dto.TestRequest;
import com.codeless.api.automation.service.TestService;
import com.codeless.api.automation.util.DefaultValueUtil;
import java.security.Principal;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
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
  public ResponseEntity<Void> updateTest(@RequestBody @Valid TestRequest test, Principal principal) {
    testService.updateTest(test, principal.getName());
    return ResponseEntity
        .noContent()
        .build();
  }

  @PostMapping
  public ResponseEntity<Void> createTest(@RequestBody @Valid TestRequest test,
      Principal principal) {
    testService.createTest(test, principal.getName());
    return ResponseEntity
        .noContent()
        .build();
  }

  @GetMapping
  public PageRequest<TestRequest> getAllTests(
      @RequestParam(name = "max_results", defaultValue = "25")
      @Min(value = 1, message="max_results is positive number, min 1 is required") Integer maxResults,
      @RequestParam(name = "next_token", required = false) @Size(max = 200) String nextToken,
      Principal principal) {
    return testService.getAllTests(
        DefaultValueUtil.getMaxResultsOrDefault(maxResults),
        nextToken,
        principal.getName());
  }

  @GetMapping(path = "/{testId}")
  public TestRequest getTest(@PathVariable @Size(min = 40, max = 40) String testId, Principal principal) {
    return testService.getTest(testId, principal.getName());
  }

  @DeleteMapping(path = "/{testId}")
  public void deleteTest(@PathVariable @Size(min = 40, max = 40) String testId, Principal principal) {
    testService.deleteTest(testId, principal.getName());
  }

}
