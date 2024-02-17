package com.codeless.api.automation.util;

import com.codeless.api.automation.dto.NextToken;
import com.codeless.api.automation.exception.ApiException;
import java.util.Objects;
import org.springframework.http.HttpStatus;

public class ApiValidationUtil {

  public static void validateNextTokenInListByCustomerId(NextToken nextToken) {
    if (Objects.isNull(nextToken)) {
      return;
    }
    if (Objects.isNull(nextToken.getId()) || Objects.isNull(nextToken.getCreated())) {
      throw new ApiException("Invalid next token!", HttpStatus.BAD_REQUEST.value());
    }
  }

}
