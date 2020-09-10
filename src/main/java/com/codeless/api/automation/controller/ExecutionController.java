package com.codeless.api.automation.controller;


import static com.codeless.api.automation.util.RestApiConstant.EXECUTION_RESOURCE;

import com.codeless.api.automation.dto.request.Execution;
import com.codeless.api.automation.service.ExecutionService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(EXECUTION_RESOURCE)
@Validated
@AllArgsConstructor
public class ExecutionController {

  private final ExecutionService executionService;

  @RequestMapping(method = RequestMethod.POST)
  public Execution createExecution(@RequestBody @Valid Execution execution) {
    return executionService.runExecution(execution);
  }

}
