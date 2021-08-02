package com.codeless.api.automation.service;

import com.codeless.api.automation.entity.User;

public interface EmailService {

  void sendEmail(User user);
}
