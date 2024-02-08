package com.codeless.api.automation.util;

import com.codeless.api.automation.dto.NextToken;
import java.util.Map;
import java.util.Objects;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class PersistenceUtil {

  public static Map<String, AttributeValue> buildLastEvaluatedKeyForRequestByCustomerId(
      NextToken nextToken) {
    if (Objects.isNull(nextToken)) {
      return null;
    }
    return Map.of(
        "customerId", AttributeValue.builder().s(nextToken.getCustomerId()).build(),
        "created", AttributeValue.builder().s(nextToken.getCreated()).build(),
        "id", AttributeValue.builder().s(nextToken.getId()).build());
  }

  public static NextToken buildNextTokenForRequestByCustomerId(
      Map<String, AttributeValue> lastEvaluatedKey) {
    if (Objects.isNull(lastEvaluatedKey)) {
      return null;
    }
    return NextToken.builder()
        .customerId(lastEvaluatedKey.get("customerId").s())
        .created(lastEvaluatedKey.get("created").s())
        .id(lastEvaluatedKey.get("id").s())
        .build();
  }

}
