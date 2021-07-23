package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.service.EmailService;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender javaMailSender;

  @Override
  public void sendEmail(SimpleMailMessage simpleMailMessage) {
    log.info("Sending email message to : '{}' ", Arrays.toString(simpleMailMessage.getTo()));
    javaMailSender.send(simpleMailMessage);
  }
}
