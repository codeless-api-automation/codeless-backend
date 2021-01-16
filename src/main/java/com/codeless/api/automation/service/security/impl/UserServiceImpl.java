package com.codeless.api.automation.service.security.impl;

import com.codeless.api.automation.dto.AppUser;
import com.codeless.api.automation.entity.security.User;
import com.codeless.api.automation.entity.security.UserRole;
import com.codeless.api.automation.repository.UserRepository;
import com.codeless.api.automation.service.security.UserService;
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

  public AppUser saveUser(AppUser appUser) {
    userRepository.save(encodeUser(appUser));
    log.info("User with name: '{}' created successfully", appUser.getUsername());
    return appUser;
  }

  private User encodeUser(AppUser appUser) {
    String password = passwordEncoder.encode(appUser.getPassword());
    return new User()
        .setUsername(appUser.getUsername())
        .setPassword(password)
        .setRole(UserRole.ROLE_USER)
        .setEnabled(true)
        .setAccountNonExpired(true)
        .setCredentialsNonExpired(true)
        .setAccountNonLocked(true);
  }
}
