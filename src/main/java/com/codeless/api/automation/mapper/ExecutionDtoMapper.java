package com.codeless.api.automation.mapper;

import com.codeless.api.automation.entity.Execution;
import com.codeless.api.automation.entity.Region;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.repository.RegionRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExecutionDtoMapper implements
    Mapper<com.codeless.api.automation.dto.Execution, Execution> {

  private final TestDtoToTestMapper testDtoMapper;
  private final RegionRepository regionRepository;

  @Override
  public Execution map(com.codeless.api.automation.dto.Execution source) {
    Region region = regionRepository.findByCity(source.getRegion().getCity())
        .orElseThrow(() -> new ApiException(
            String.format("Region with city name '%s' is not found.", source.getRegion().getCity()),
            HttpStatus.BAD_REQUEST.value()));

    Execution preparedExecution = new Execution();
    preparedExecution.setName(source.getName());
    preparedExecution.setType(source.getType());
    preparedExecution.setStatus(source.getExecutionStatus());
    preparedExecution.setRegion(region);
    preparedExecution.setTests(source.getTests().stream()
        .map(testDtoMapper::map)
        .collect(Collectors.toSet()));
    return preparedExecution;
  }
}
