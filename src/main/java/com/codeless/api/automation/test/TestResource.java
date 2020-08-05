package com.codeless.api.automation.test;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tests")
@CrossOrigin
public class TestResource {

  @RequestMapping(method = RequestMethod.POST)
  public void createTest(@RequestBody TestDto testDto) {
    System.out.println(testDto.getName());
  }

}
