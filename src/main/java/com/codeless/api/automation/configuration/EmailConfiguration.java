package com.codeless.api.automation.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class EmailConfiguration {

  @Value("${codeless.mail.host}")
  private String host;
  @Value("${codeless.mail.from}")
  private String from;
  @Value("${codeless.mail.subject}")
  private String subject;
  @Value("${codeless.mail.template}")
  private String template;
  @Value("${codeless.mail.verification-success-redirect}")
  private String verificationSuccessRedirect;
}
