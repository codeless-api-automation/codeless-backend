package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.configuration.AwsConfiguration;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.service.ExecutionClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvocationType;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.ResourceConflictException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExecutionClientImpl implements ExecutionClient {

  private final ObjectMapper objectMapper;
  private final Map<Region, LambdaClient> lambdaClientByRegion;
  private final AwsConfiguration awsConfiguration;

  public void execute(String region, Map<String, String> payload) {

    LambdaClient lambdaClient = lambdaClientByRegion.get(Region.of(region));
    if (lambdaClient == null) {
      throw new ApiException("Requested region is not available yet",
          HttpStatus.BAD_REQUEST.value());
    }

    try {
      lambdaClient.invoke(InvokeRequest.builder()
          .functionName(awsConfiguration.getFunctionName())
          .invocationType(InvocationType.EVENT)
          .payload(SdkBytes.fromUtf8String(toString(payload)))
          .build());
    } catch (ResourceConflictException ex) {
      if (isLambdaStarting(ex)) {
        throw new ApiException("The operation cannot be performed at this time. "
            + "We are preparing compute resource. Try again later.",
            HttpStatus.INTERNAL_SERVER_ERROR.value());
      }
      throw ex;
    }
  }

  private boolean isLambdaStarting(ResourceConflictException ex) {
    return Integer.valueOf(409).equals(ex.statusCode())
        && ex.awsErrorDetails().errorMessage()
        .contains("The operation cannot be performed at this time. "
            + "The function is currently in the following state");
  }

  private String toString(Map<String, String> payload) {
    try {
      return objectMapper.writeValueAsString(payload);
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

}
