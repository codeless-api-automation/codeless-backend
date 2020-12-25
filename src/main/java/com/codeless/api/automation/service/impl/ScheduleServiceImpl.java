package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.configuration.DataFlowConfiguration;
import com.codeless.api.automation.domain.Test;
import com.codeless.api.automation.dto.Schedule;
import com.codeless.api.automation.mapper.TestDtoToTestDomainMapper;
import com.codeless.api.automation.service.ScheduleService;
import com.codeless.api.automation.service.TestSuiteBuilderService;
import com.codeless.api.automation.util.TaskLaunchArgumentsService;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.dataflow.rest.client.SchedulerOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

  private final SchedulerOperations schedulerOperations;
  private final DataFlowConfiguration dataFlowConfiguration;
  private final TaskLaunchArgumentsService taskLaunchArgumentsService;
  private final TestSuiteBuilderService testSuiteBuilderService;
  private final TestDtoToTestDomainMapper testDtoToTestDomainMapper;

  @Override
  public Schedule runSchedule(Schedule schedule) {
    List<Test> tests = schedule.getTests().stream()
        .map(testDtoToTestDomainMapper::map)
        .collect(Collectors.toList());

    schedulerOperations.schedule(
        schedule.getName(),
        dataFlowConfiguration.getDefinitionName(),
        taskLaunchArgumentsService.getProperties(),
        ImmutableList.of(
            taskLaunchArgumentsService.getTestSuiteArgument(testSuiteBuilderService.build(tests))));

    return Schedule.builder()
        .name(schedule.getName())
        .region(schedule.getRegion())
        .tests(schedule.getTests())
        .build();
  }
}
