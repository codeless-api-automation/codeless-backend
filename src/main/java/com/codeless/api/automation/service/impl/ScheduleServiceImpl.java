package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.dto.Page;
import com.codeless.api.automation.dto.Schedule;
import com.codeless.api.automation.entity.ExecutionType;
import com.codeless.api.automation.mapper.ScheduleDtoMapper;
import com.codeless.api.automation.mapper.ScheduleMapper;
import com.codeless.api.automation.mapper.TimerDtoToContextMapper;
import com.codeless.api.automation.repository.ScheduleRepository;
import com.codeless.api.automation.service.CronExpressionBuilderService;
import com.codeless.api.automation.service.ScheduleService;
import com.codeless.api.automation.util.TaskLaunchArgumentsService;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

  private final SchedulerClientImpl schedulerClient;
  private final TaskLaunchArgumentsService taskLaunchArgumentsService;
  private final ScheduleDtoMapper scheduleDtoMapper;
  private final ScheduleMapper scheduleMapper;
  private final TimerDtoToContextMapper timerDtoToContextMapper;
  private final ScheduleRepository scheduleRepository;
  private final CronExpressionBuilderService cronExpressionBuilderService;

  @Override
  @Transactional
  public Schedule runSchedule(Schedule schedule, Principal principal) {
    final String scheduleUuid = UUID.randomUUID().toString();

    com.codeless.api.automation.entity.Schedule preparedSchedule =
        scheduleDtoMapper.map(schedule);
    preparedSchedule.setUuid(scheduleUuid);
    preparedSchedule.setUsername(principal.getName());

    com.codeless.api.automation.entity.Schedule persistedSchedule =
        scheduleRepository.save(preparedSchedule);

    Map<String, String> payload = new HashMap<>();
    payload.putAll(taskLaunchArgumentsService
        .getScheduleIdArgument(persistedSchedule.getId()));
    payload.putAll(taskLaunchArgumentsService
        .getExecutionTypeArgument(ExecutionType.SCHEDULED_EXECUTION.getName()));

    schedulerClient.createSchedule(
        persistedSchedule.getRegion().getAwsCloudRegion(),
        scheduleUuid,
        String.format("cron(%s)",
        cronExpressionBuilderService.buildCronExpression(timerDtoToContextMapper.map(schedule.getTimer()))),
        payload);

    return scheduleMapper.map(persistedSchedule);
  }

  @Override
  public Page<Schedule> getSchedules(Integer page, Integer size, Principal principal) {
    org.springframework.data.domain.Page<com.codeless.api.automation.entity.Schedule> schedules =
        scheduleRepository.findAllByUsername(principal.getName(), PageRequest.of(page, size));

    List<Schedule> schedulesDto = schedules.getContent().stream()
        .map(scheduleMapper::map)
        .collect(Collectors.toList());

    return Page.<Schedule>builder()
        .items(schedulesDto)
        .build();
  }
}
