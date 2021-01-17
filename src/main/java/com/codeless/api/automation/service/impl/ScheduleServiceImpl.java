package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.configuration.DataFlowConfiguration;
import com.codeless.api.automation.domain.Test;
import com.codeless.api.automation.dto.Page;
import com.codeless.api.automation.dto.Schedule;
import com.codeless.api.automation.entity.ExecutionType;
import com.codeless.api.automation.mapper.ScheduleDtoMapper;
import com.codeless.api.automation.mapper.ScheduleMapper;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  private final ScheduleMapper scheduleMapper;
  private final TimerDtoToContextMapper timerDtoToContextMapper;
  private final ScheduleRepository scheduleRepository;
  private final CronExpressionBuilderService cronExpressionBuilderService;

  @Override
  @Transactional
  public Schedule runSchedule(Schedule schedule) {
    List<Test> tests = schedule.getTests().stream()
        .map(testDtoToTestDomainMapper::map)
        .collect(Collectors.toList());

    final String internalScheduleName =
        UUID.nameUUIDFromBytes(schedule.getScheduleName().getBytes(StandardCharsets.UTF_8))
            .toString();

    com.codeless.api.automation.entity.Schedule preparedSchedule =
        scheduleDtoMapper.map(schedule);
    preparedSchedule.setInternalName(internalScheduleName);

    com.codeless.api.automation.entity.Schedule persistedSchedule =
        scheduleRepository.save(preparedSchedule);

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
            .add(taskLaunchArgumentsService
                .getScheduleIdArgument(persistedSchedule.getId()))
            .add(taskLaunchArgumentsService
                .getExecutionTypeArgument(ExecutionType.SCHEDULED_EXECUTION.getName()))
            .build());

    return Schedule.builder()
        .scheduleName(schedule.getScheduleName())
        .region(schedule.getRegion())
        .tests(schedule.getTests())
        .timer(schedule.getTimer())
        .id(persistedSchedule.getId())
        .build();
  }

  @Override
  public Page<Schedule> getSchedules(Integer page, Integer size) {
    org.springframework.data.domain.Page<com.codeless.api.automation.entity.Schedule> schedules =
        scheduleRepository.findAll(PageRequest.of(page, size));

    List<Schedule> schedulesDto = schedules.getContent().stream()
        .map(scheduleMapper::map)
        .collect(Collectors.toList());

    return Page.<Schedule>builder()
        .items(schedulesDto)
        .build();
  }
}
