package com.codeless.api.automation.util;

import com.codeless.api.automation.dto.UserVerification;
import com.codeless.api.automation.entity.User;
import com.codeless.api.automation.exception.ApiException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import org.springframework.http.HttpStatus;

public final class VerificationUtil {

  private static final String JOINER = ":::";
  private static final int TOKEN_TTL_MINUTES = 45;

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME
      .withZone(
          ZoneId.from(ZoneOffset.UTC));

  public static String createVerificationToken(User user) {
    return Base64.getUrlEncoder().encodeToString(
        String.join(JOINER, user.getUuid(), user.getUsername(),
            DATE_TIME_FORMATTER.format(LocalDateTime.now()))
            .getBytes());
  }

  public static UserVerification verifyExpirationAndGetUserFromToken(String token) {
    UserVerification userVerification = new UserVerification();
    try {
      String[] tokenDetails = new String(Base64.getUrlDecoder().decode(token.getBytes()))
          .split(JOINER);
      userVerification.setUuid(tokenDetails[0]).setEmail(tokenDetails[1]).setDate(tokenDetails[2]);
    } catch (Exception ex) {
      throw new ApiException("Invalid verification token.", HttpStatus.FORBIDDEN.value());
    }
    verifyTokenExpiration(userVerification.getDate());
    return userVerification;
  }

  private static void verifyTokenExpiration(String tokenIssueTime) {
    if (
        LocalDateTime.parse(tokenIssueTime, DATE_TIME_FORMATTER).plusMinutes(TOKEN_TTL_MINUTES)
            .isBefore(LocalDateTime.now())) {
      throw new ApiException("Verification token is expired", HttpStatus.FORBIDDEN.value());
    }
  }
}
