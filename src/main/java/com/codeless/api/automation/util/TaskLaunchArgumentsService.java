package com.codeless.api.automation.util;

import com.codeless.api.automation.configuration.DataFlowConfiguration;
import com.codeless.api.automation.constant.ArgumentConstant;
import com.google.common.collect.ImmutableMap;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskLaunchArgumentsService {

  private static final String BACK_OFF_LIMIT = "deployer.%s.kubernetes.backoffLimit";
  private static final String ARGUMENT_FORMAT = "%s=%s";

  private final DataFlowConfiguration dataFlowConfiguration;

  public String getTestSuiteArgument(String testSuite) {
    byte[] encodedTestSuite = Base64.getEncoder().encode(testSuite.getBytes());
    final String testSuiteTaskArgument = "suite=%s";
    return String.format(testSuiteTaskArgument, new String(encodedTestSuite));
  }

  public Map<String, String> getProperties() {
    return ImmutableMap.<String, String>builder()
        .put(String.format(BACK_OFF_LIMIT, dataFlowConfiguration.getDefinitionName()), "0")
        .build();
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
