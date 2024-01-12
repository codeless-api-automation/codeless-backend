package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.configuration.EmailConfiguration;
import com.codeless.api.automation.entity.User;
import com.codeless.api.automation.service.ContentBuilderService;
import com.codeless.api.automation.service.EmailService;
import com.codeless.api.automation.service.VerificationService;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

  private static final String VERIFICATION_URL = "VERIFICATION_URL";

  private final VerificationService verificationService;
  private final ContentBuilderService contentBuilderService;
  private final JavaMailSender javaMailSender;
  private final EmailConfiguration emailConfig;

  @Override
  public void sendEmail(User user) {
    MimeMessage mimeMessage = generateVerificationMessage(user);
    log.info("Sending email message to : '{}' ", user.getUsername());
    javaMailSender.send(mimeMessage);
  }

  public MimeMessage generateVerificationMessage(User user) {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
      mimeMessageHelper.setTo(user.getUsername());
      mimeMessageHelper.setSubject(emailConfig.getSubject());
      mimeMessageHelper.setFrom(emailConfig.getFrom());

      String verificationToken = verificationService.createVerificationToken(user);
      String verifiedHost = emailConfig.getHost().endsWith("/")
          ? StringUtils.substring(emailConfig.getHost(), 0, emailConfig.getHost().length() - 1)
          : emailConfig.getHost();
      String verificationUrl =
          verifiedHost + "/api/verify?verification-token=" + verificationToken;
      String messageContent = contentBuilderService.buildContent(emailConfig.getTemplate(),
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
