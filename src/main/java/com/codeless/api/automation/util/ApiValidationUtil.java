package com.codeless.api.automation.util;

import com.codeless.api.automation.dto.NextToken;
import com.codeless.api.automation.exception.ApiException;
import java.util.Objects;
import org.springframework.http.HttpStatus;

public class ApiValidationUtil {

  public static void validateNextTokenOwnership(NextToken nextToken, String customerId) {
    if (Objects.isNull(nextToken)) {
      return;
    }

    if (Objects.isNull(nextToken.getCustomerId())) {
      throw new ApiException(RestApiConstant.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED.value());
    }

    if (!customerId.equals(nextToken.getCustomerId())) {
      throw new ApiException(RestApiConstant.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED.value());
    }
  }

  public static void validateNextTokenForRequestByCustomerId(NextToken nextToken) {
    if (Objects.isNull(nextToken)) {
      return;
    }
    if (Objects.isNull(nextToken.getCustomerId())
        || Objects.isNull(nextToken.getId())
        || Objects.isNull(nextToken.getCreated())) {
      throw new ApiException("Invalid next token!", HttpStatus.BAD_REQUEST.value());
    }
  }

}
