package com.codeless.api.automation.controller;

import com.codeless.api.automation.dto.UserRegistration;
import com.codeless.api.automation.entity.User;
import com.codeless.api.automation.service.EmailService;
import com.codeless.api.automation.service.UserService;
import com.codeless.api.automation.util.EmailMessageUtil;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final EmailService emailService;

  @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
  public void register(HttpServletRequest httpServletRequest, @Valid @RequestBody UserRegistration userRegistration) {
    User user = userService.saveUser(userRegistration);
    emailService.sendEmail(EmailMessageUtil.generateVerificationMessage(user, httpServletRequest));
  }

  @GetMapping(value = "/verify")
  public ResponseEntity<String> confirm(@RequestParam("verification-token") String verificationToken) {
    userService.verifyUser(verificationToken);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body("User verified successfully.");
  }
}
