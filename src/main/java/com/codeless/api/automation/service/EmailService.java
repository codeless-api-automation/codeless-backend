package com.codeless.api.automation.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

  void sendEmail(SimpleMailMessage simpleMailMessage);
}
