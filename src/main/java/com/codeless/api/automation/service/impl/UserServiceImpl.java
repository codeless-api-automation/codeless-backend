package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.dto.UserRegistration;
import com.codeless.api.automation.dto.UserVerification;
import com.codeless.api.automation.entity.User;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.repository.UserRepository;
import com.codeless.api.automation.service.UserService;
import com.codeless.api.automation.service.VerificationService;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final VerificationService verificationService;
  private final PasswordEncoder passwordEncoder;


  public User saveUser(UserRegistration userRegistration) {
    User user = userRepository.get(userRegistration.getEmail());
    if (Objects.nonNull(user) && !user.isEnabled()) {
      throw new ApiException("Verification already sent, please check your email.",
          HttpStatus.BAD_REQUEST.value());
    }
    if (Objects.nonNull(user) && user.isEnabled()) {
      throw new ApiException("User with this email is already in use. Try another.",
          HttpStatus.BAD_REQUEST.value());
    }
    User encodedUser = encodeUser(userRegistration);
    userRepository.create(encodedUser);
    log.info("User with name: '{}' created successfully", encodedUser.getUsername());
    return encodedUser;
  }

  public User verifyUser(String verificationToken) {
    UserVerification userVerification = verificationService.getUserVerification(verificationToken);
    User user = userRepository.get(userVerification.getEmail());
    if (Objects.isNull(user) || (!user.getToken().equals(userVerification.getUuid()))) {
      throw new ApiException("There is no appropriate user found, please register.",
          HttpStatus.NOT_FOUND.value());
    }
    if (verificationService.isTokenExpired(userVerification.getDate())) {
      return user;
    }
    user.setEnabled(true);
    userRepository.put(user);
    return user;
  }

  private User encodeUser(UserRegistration userRegistration) {
    String password = passwordEncoder.encode(userRegistration.getPassword());
    User user = new User();
    user.setToken(UUID.randomUUID().toString());
    user.setUsername(userRegistration.getEmail());
    user.setPassword(password);
    user.setFirstName(userRegistration.getFirstName());
    user.setLastName(userRegistration.getLastName());
    user.setEnabled(false);
    user.setAccountNonExpired(true);
    user.setCredentialsNonExpired(true);
    user.setAccountNonLocked(true);
    return user;
  }
}
