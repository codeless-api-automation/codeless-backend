package com.codeless.api.automation.service;


import com.codeless.api.automation.dto.Execution;
import com.codeless.api.automation.dto.Page;

public interface ExecutionService {

  Execution runExecution(Execution execution);

  Page<Execution> getExecutions(Integer page, Integer size);
}
