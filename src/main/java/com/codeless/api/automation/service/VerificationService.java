package com.codeless.api.automation.service;

import com.codeless.api.automation.dto.UserVerification;
import com.codeless.api.automation.entity.User;

public interface VerificationService {

  String createVerificationToken(User user);
  UserVerification getUserVerification(String verificationToken);
  boolean isTokenExpired(String tokenIssueTime);
}
