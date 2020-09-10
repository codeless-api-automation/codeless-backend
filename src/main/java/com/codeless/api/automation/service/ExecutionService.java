package com.codeless.api.automation.service;


import com.codeless.api.automation.dto.request.Execution;

public interface ExecutionService {

  Execution runExecution(Execution execution);
}
