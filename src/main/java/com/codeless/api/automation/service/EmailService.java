package com.codeless.api.automation.service;

import com.codeless.api.automation.entity.User;
import javax.servlet.http.HttpServletRequest;

public interface EmailService {

  void sendEmail(User user, HttpServletRequest httpServletRequest);
}
