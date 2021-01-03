package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.configuration.DataFlowConfiguration;
import com.codeless.api.automation.context.CronExpressionContext;
import com.codeless.api.automation.domain.Test;
import com.codeless.api.automation.dto.Schedule;
import com.codeless.api.automation.mapper.ScheduleDtoMapper;
import com.codeless.api.automation.mapper.TestDtoToTestDomainMapper;
import com.codeless.api.automation.mapper.TimerDtoToContextMapper;
import com.codeless.api.automation.repository.ScheduleRepository;
import com.codeless.api.automation.service.CronExpressionBuilderService;
import com.codeless.api.automation.service.ScheduleService;
import com.codeless.api.automation.service.TestSuiteBuilderService;
import com.codeless.api.automation.util.TaskLaunchArgumentsService;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.dataflow.rest.client.SchedulerOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

  private static final String CRON_EXPRESSION = "scheduler.cron.expression";

  private final SchedulerOperations schedulerOperations;
  private final DataFlowConfiguration dataFlowConfiguration;
  private final TaskLaunchArgumentsService taskLaunchArgumentsService;
  private final TestSuiteBuilderService testSuiteBuilderService;
  private final TestDtoToTestDomainMapper testDtoToTestDomainMapper;
  private final ScheduleDtoMapper scheduleDtoMapper;
  private final TimerDtoToContextMapper timerDtoToContextMapper;
  private final ScheduleRepository scheduleRepository;
  private final CronExpressionBuilderService cronExpressionBuilderService;

  @Override
  public Schedule runSchedule(Schedule schedule) {
    List<Test> tests = schedule.getTests().stream()
        .map(testDtoToTestDomainMapper::map)
        .collect(Collectors.toList());

    String internalScheduleName =
        UUID.nameUUIDFromBytes(schedule.getScheduleName().getBytes(StandardCharsets.UTF_8))
            .toString();

    schedulerOperations.schedule(internalScheduleName,
        dataFlowConfiguration.getTaskName(),
        ImmutableMap.<String, String>builder()
            .putAll(taskLaunchArgumentsService.getProperties())
            .put(CRON_EXPRESSION, cronExpressionBuilderService
                .buildCronExpression(timerDtoToContextMapper.map(schedule.getTimer())))
            .build(),
        ImmutableList.<String>builder()
            .add(taskLaunchArgumentsService
                .getTestSuiteArgument(testSuiteBuilderService.build(tests)))
            .build());

    com.codeless.api.automation.entity.Schedule preparedSchedule =
        scheduleDtoMapper.map(schedule);
    preparedSchedule.setInternalName(internalScheduleName);

    com.codeless.api.automation.entity.Schedule persistedSchedule =
        scheduleRepository.save(preparedSchedule);

    return Schedule.builder()
        .scheduleName(schedule.getScheduleName())
        .region(schedule.getRegion())
        .tests(schedule.getTests())
        .timer(schedule.getTimer())
        .id(persistedSchedule.getId())
        .build();
  }
}
