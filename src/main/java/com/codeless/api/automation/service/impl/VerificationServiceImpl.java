package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.dto.UserVerification;
import com.codeless.api.automation.entity.User;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.service.VerificationService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

  private static final String JOINER = ":::";
  private static final int TOKEN_TTL_MINUTES = 15;

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME
      .withZone(ZoneId.from(ZoneOffset.UTC));

  @Override
  public String createVerificationToken(User user) {
    return Base64.getUrlEncoder().encodeToString(
        String.join(JOINER, user.getToken(), user.getUsername(),
            DATE_TIME_FORMATTER.format(LocalDateTime.now())).getBytes());
  }

  @Override
  public UserVerification getUserVerification(String verificationToken) {
    UserVerification userVerification = new UserVerification();
    try {
      String[] tokenDetails = new String(
          Base64.getUrlDecoder().decode(verificationToken.getBytes())).split(JOINER);
      userVerification.setUuid(tokenDetails[0]).setEmail(tokenDetails[1]).setDate(tokenDetails[2]);
    } catch (Exception ex) {
      throw new ApiException("Invalid verification token.", HttpStatus.FORBIDDEN.value());
    }
    return userVerification;
  }

  @Override
  public boolean isTokenExpired(String tokenIssueTime) {
    return
        LocalDateTime.parse(tokenIssueTime, DATE_TIME_FORMATTER).plusDays(TOKEN_TTL_MINUTES)
            .isBefore(LocalDateTime.now());
  }
}
