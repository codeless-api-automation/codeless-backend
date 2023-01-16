package com.codeless.api.automation.util;

import com.codeless.api.automation.constant.ArgumentConstant;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskLaunchArgumentsService {

  private static final String ARGUMENT_FORMAT = "%s=%s";

  public String getTestSuiteArgument(String testSuite) {
    byte[] encodedTestSuite = Base64.getEncoder().encode(testSuite.getBytes());
    final String testSuiteTaskArgument = "suite=%s";
    return String.format(testSuiteTaskArgument, new String(encodedTestSuite));
  }

  public String getExecutionTypeArgument(String executionType) {
    return String.format(ARGUMENT_FORMAT, ArgumentConstant.EXECUTION_TYPE, executionType);
  }

  public String getScheduleIdArgument(Long scheduleId) {
    return String.format(ARGUMENT_FORMAT, ArgumentConstant.SCHEDULE_ID, scheduleId);
  }

  public String getExecutionIdArgument(Long executionId) {
    return String.format(ARGUMENT_FORMAT, ArgumentConstant.EXECUTION_ID, executionId);
  }

}
