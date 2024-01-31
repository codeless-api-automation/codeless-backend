package com.codeless.api.automation.util;

import com.codeless.api.automation.constant.ArgumentConstant;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskLaunchArgumentsService {

  public Map<String, String> getExecutionTypeArgument(String executionType) {
    return ImmutableMap.of(ArgumentConstant.EXECUTION_TYPE, executionType);
  }

  public Map<String, String> getScheduleIdArgument(String scheduleId) {
    return ImmutableMap.of(ArgumentConstant.SCHEDULE_ID, String.valueOf(scheduleId));
  }

  public Map<String, String> getExecutionIdArgument(String executionId) {
    return ImmutableMap.of(ArgumentConstant.EXECUTION_ID, String.valueOf(executionId));
  }

}
