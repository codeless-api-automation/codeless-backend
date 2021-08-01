package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.entity.User;
import com.codeless.api.automation.service.ContentBuilderService;
import com.codeless.api.automation.service.EmailService;
import com.codeless.api.automation.service.VerificationService;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

  private static final String SUBJECT_TEMPLATE = "Codeless API Automation registration verification";
  private static final String FROM_EMAIL = "api.monitor.bot@gmail.com";
  private static final String NOTIFICATION_EMAIL_TEMPLATE = "email-verification";
  private static final String VERIFICATION_URL = "VERIFICATION_URL";

  private final VerificationService verificationService;
  private final ContentBuilderService contentBuilderService;
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
      mimeMessageHelper.setSubject(SUBJECT_TEMPLATE);
      mimeMessageHelper.setFrom(FROM_EMAIL);

      String verificationToken = verificationService.createVerificationToken(user);
      String baseUrl = ServletUriComponentsBuilder.fromRequestUri(httpServletRequest)
          .replacePath("codeless")
          .build()
          .toUriString();
      String verificationUrl = baseUrl + "/verify?verification-token=" + verificationToken;
      String messageContent = contentBuilderService.buildContent(NOTIFICATION_EMAIL_TEMPLATE,
          Objects.requireNonNull(getMailContext(verificationUrl)));
      mimeMessage.setContent(messageContent, "text/html");
    } catch (MessagingException mEx) {
      log.error("Unable to create mime message.", mEx);
    }
    return mimeMessage;
  }

  private Map<String, String> getMailContext(String url) {
    Map<String, String> context = new HashMap<>();
    context.put(VERIFICATION_URL, url);
    return context;
  }
}
