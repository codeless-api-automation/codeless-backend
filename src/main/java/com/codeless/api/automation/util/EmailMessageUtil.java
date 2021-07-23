package com.codeless.api.automation.util;

import com.codeless.api.automation.entity.User;
import javax.servlet.http.HttpServletRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public final class EmailMessageUtil {

  private EmailMessageUtil() {
  }

  public static SimpleMailMessage generateVerificationMessage(User user,
      HttpServletRequest httpServletRequest) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(user.getUsername());
    simpleMailMessage.setSubject("Complete Registration!");
    simpleMailMessage.setFrom("codelessautomation@gmail.com");
    String verificationToken = VerificationUtil.createVerificationToken(user);
    String baseUrl = ServletUriComponentsBuilder.fromRequestUri(httpServletRequest)
        .replacePath("codeless")
        .build()
        .toUriString();
    simpleMailMessage.setText("To verify your account, please click here : "
        + baseUrl + "/verify?verification-token=" + verificationToken);
    return simpleMailMessage;
  }
}
