package com.codeless.api.automation.controller;


import static com.codeless.api.automation.util.RestApiConstant.EXECUTION_RESOURCE;

import com.codeless.api.automation.dto.request.Execution;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class ExecutionController {

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Void> createExecution(@RequestBody @Valid Execution execution) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(null);
  }

}
