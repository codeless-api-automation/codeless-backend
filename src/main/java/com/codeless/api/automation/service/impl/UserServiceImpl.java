package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.dto.UserRegistration;
import com.codeless.api.automation.entity.User;
import com.codeless.api.automation.entity.UserRole;
import com.codeless.api.automation.repository.UserRepository;
import com.codeless.api.automation.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) {
    UserDetails user = userRepository.findByUsername(username);
    if (Objects.nonNull(user)) {
      log.info("Find user with name: '{}'", username);
      return user;
    }
    throw new UsernameNotFoundException("User not exist with name :" + username);
  }

  public void saveUser(UserRegistration userRegistration) {
    userRepository.save(encodeUser(userRegistration));
    log.info("User with name: '{}' created successfully", userRegistration.getUsername());
  }

  private User encodeUser(UserRegistration userRegistration) {
    String password = passwordEncoder.encode(userRegistration.getPassword());
    return new User()
        .setUsername(userRegistration.getUsername())
        .setPassword(password)
        .setRole(UserRole.ROLE_USER)
        .setEnabled(true)
        .setAccountNonExpired(true)
        .setCredentialsNonExpired(true)
        .setAccountNonLocked(true);
  }
}