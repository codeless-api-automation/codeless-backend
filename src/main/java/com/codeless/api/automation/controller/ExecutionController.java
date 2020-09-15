package com.codeless.api.automation.controller;


import static com.codeless.api.automation.util.RestApiConstant.EXECUTION_RESOURCE;

import com.codeless.api.automation.dto.Execution;
import com.codeless.api.automation.dto.Page;
import com.codeless.api.automation.service.ExecutionService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(EXECUTION_RESOURCE)
@Validated
@AllArgsConstructor
public class ExecutionController {

  private final ExecutionService executionService;

  @PostMapping
  public Execution createExecution(@RequestBody @Valid Execution execution) {
    return executionService.runExecution(execution);
  }

  @GetMapping
  public Page<Execution> getExecutions(@RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "5") Integer size) {
    return executionService.getExecutions(page, size);
  }

}
