package com.codeless.api.automation.test;

import static com.codeless.api.automation.util.RestApiConstant.SINGLE_TEST_RESOURCE_WITH_ROOT_PATH;
import static com.codeless.api.automation.util.RestApiConstant.TEST_RESOURCE;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(TEST_RESOURCE)
@Validated
public class TestResource {

  @Autowired
  private TestService testService;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Void> createTest(@RequestBody @Valid TestDto testDto) {
    Test createdTest = testService.saveTest(testDto);
    return ResponseEntity
        .created(URI.create(SINGLE_TEST_RESOURCE_WITH_ROOT_PATH + createdTest.getId()))
        .build();
  }

}
