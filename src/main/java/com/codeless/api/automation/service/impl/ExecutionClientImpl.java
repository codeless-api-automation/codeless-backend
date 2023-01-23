package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.configuration.AwsConfiguration;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.service.ExecutionClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvocationType;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;

@Service
@RequiredArgsConstructor
public class ExecutionClientImpl implements ExecutionClient {

  private final ObjectMapper objectMapper;
  private final Map<Region, LambdaClient> lambdaClientByRegion;
  private final AwsConfiguration awsConfiguration;

  public void execute(String region, Map<String, String> payload) {


    LambdaClient lambdaClient = lambdaClientByRegion.get(Region.of(region));
    if (lambdaClient == null) {
      throw new ApiException("Requested region is not available yet", HttpStatus.BAD_REQUEST.value());
    }

    lambdaClient.invoke(InvokeRequest.builder()
        .functionName(awsConfiguration.getFunctionName())
        .invocationType(InvocationType.EVENT)
        .payload(SdkBytes.fromUtf8String(toString(payload)))
        .build());

  }

  private String toString(Map<String, String> payload) {
    try {
      return objectMapper.writeValueAsString(payload);
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

}
