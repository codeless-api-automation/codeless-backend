package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.dto.request.Execution;
import com.codeless.api.automation.mapper.ExecutionDtoMapper;
import com.codeless.api.automation.repository.ExecutionRepository;
import com.codeless.api.automation.service.ExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExecutionServiceImpl implements ExecutionService {

  @Autowired
  private ExecutionRepository executionRepository;
  @Autowired
  private ExecutionDtoMapper executionDtoMapper;

  @Override
  public void runExecution(Execution execution) {
    com.codeless.api.automation.entity.Execution createdExecution =
        executionRepository.save(executionDtoMapper.map(execution));
  }
}
