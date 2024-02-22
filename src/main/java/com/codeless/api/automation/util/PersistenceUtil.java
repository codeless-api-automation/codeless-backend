package com.codeless.api.automation.util;

import com.codeless.api.automation.dto.NextToken;
import java.util.Map;
import java.util.Objects;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class PersistenceUtil {

  public static Map<String, AttributeValue> buildLastEvaluatedKeyInListByCustomerId(
      NextToken nextToken, String customerId) {
    if (Objects.isNull(nextToken)) {
      return null;
    }
    return Map.of(
        "customerId", AttributeValue.builder().s(customerId).build(),
        "created", AttributeValue.builder().s(nextToken.getCreated()).build(),
        "id", AttributeValue.builder().s(nextToken.getId()).build());
  }

  public static NextToken buildNextTokenInListByCustomerId(
      Map<String, AttributeValue> lastEvaluatedKey) {
    if (Objects.isNull(lastEvaluatedKey)) {
      return null;
    }
    return NextToken.builder()
        .created(lastEvaluatedKey.get("created").s())
        .id(lastEvaluatedKey.get("id").s())
        .build();
  }

  public static Map<String, AttributeValue> buildLastEvaluatedKeyInListByScheduleId(
      NextToken nextToken, String scheduleId) {
    if (Objects.isNull(nextToken)) {
      return null;
    }
    return Map.of(
        "scheduleId", AttributeValue.builder().s(scheduleId).build(),
        "created", AttributeValue.builder().s(nextToken.getCreated()).build(),
        "id", AttributeValue.builder().s(nextToken.getId()).build());
  }

  public static NextToken buildNextTokenInListByScheduleId(
      Map<String, AttributeValue> lastEvaluatedKey) {
    if (Objects.isNull(lastEvaluatedKey)) {
      return null;
    }
    return NextToken.builder()
        .created(lastEvaluatedKey.get("created").s())
        .id(lastEvaluatedKey.get("id").s())
        .build();
  }

}
