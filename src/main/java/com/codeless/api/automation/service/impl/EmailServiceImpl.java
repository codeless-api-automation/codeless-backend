package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.entity.User;
import com.codeless.api.automation.service.EmailService;
import com.codeless.api.automation.util.VerificationUtil;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender javaMailSender;

  @Override
  public void sendEmail(User user, HttpServletRequest httpServletRequest) {
    MimeMessage mimeMessage = generateVerificationMessage(user, httpServletRequest);
    log.info("Sending email message to : '{}' ", user.getUsername());
    javaMailSender.send(mimeMessage);
  }

  public MimeMessage generateVerificationMessage(User user, HttpServletRequest httpServletRequest) {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
      mimeMessageHelper.setTo(user.getUsername());
      mimeMessageHelper.setSubject("Codeless Automation Registration verification");
      mimeMessageHelper.setFrom("codelessautomation@gmail.com");

      String verificationToken = VerificationUtil.createVerificationToken(user);
      String baseUrl = ServletUriComponentsBuilder.fromRequestUri(httpServletRequest)
          .replacePath("codeless")
          .build()
          .toUriString();
      String verificationUrl = baseUrl + "/verify?verification-token=" + verificationToken;

      String messageContent = "<h5>Please verify your account using following <a href=\""
          + verificationUrl + "\">LINK</a></h5>"
          + " <h4 style=\"color:DodgerBlue;\">Codeless Automation <br>All rights reserved </h4>";
      mimeMessage.setContent(messageContent, "text/html");
    } catch (MessagingException mEx) {
      log.error("Unable to create mime message.", mEx);
    }
    return mimeMessage;
  }
}
