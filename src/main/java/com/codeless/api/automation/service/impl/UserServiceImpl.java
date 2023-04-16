package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.dto.UserRegistration;
import com.codeless.api.automation.dto.UserVerification;
import com.codeless.api.automation.entity.User;
import com.codeless.api.automation.entity.UserRole;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.repository.UserRepository;
import com.codeless.api.automation.service.UserService;
import com.codeless.api.automation.service.VerificationService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

  private final UserRepository userRepository;
  private final VerificationService verificationService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) {
    UserDetails user = userRepository.findByUsername(username);
    if (Objects.nonNull(user)) {
      log.info("Find user with name: '{}'", username);
      return user;
    }
    throw new UsernameNotFoundException("User does not exist with name :" + username);
  }

  public User saveUser(UserRegistration userRegistration) {
    User user = userRepository.findByUsername(userRegistration.getEmail());
    if (Objects.nonNull(user) && !user.isEnabled()) {
      throw new ApiException("Verification already sent, please check your email.",
          HttpStatus.BAD_REQUEST.value());
    }
    if (Objects.nonNull(user) && user.isEnabled()) {
      throw new ApiException("User with this email is already in use. Try another.",
          HttpStatus.BAD_REQUEST.value());
    }
    User encodedUser = encodeUser(userRegistration);
    User savedUser = userRepository.save(encodedUser);
    log.info("User with name: '{}' created successfully", savedUser.getUsername());
    return savedUser;
  }

  public User verifyUser(String verificationToken) {
    UserVerification userVerification = verificationService.getUserVerification(verificationToken);
    User user = userRepository
        .findByUuidAndUsername(userVerification.getUuid(), userVerification.getEmail());
    if (Objects.isNull(user)) {
      throw new ApiException("There is no appropriate user found, please register",
          HttpStatus.NOT_FOUND.value());
    }
    if (verificationService.isTokenExpired(userVerification.getDate())) {
      return user;
    }
    user.setEnabled(true);
    return userRepository.save(user);
  }

  private User encodeUser(UserRegistration userRegistration) {
    String password = passwordEncoder.encode(userRegistration.getPassword());
    return new User()
        .setUuid(UUID.randomUUID().toString())
        .setUsername(userRegistration.getEmail())
        .setPassword(password)
        .setFirstName(userRegistration.getFirstName())
        .setLastName(userRegistration.getLastName())
        .setRole(UserRole.ROLE_USER)
        .setEnabled(false)
        .setAccountNonExpired(true)
        .setCredentialsNonExpired(true)
        .setAccountNonLocked(true);
  }
}
