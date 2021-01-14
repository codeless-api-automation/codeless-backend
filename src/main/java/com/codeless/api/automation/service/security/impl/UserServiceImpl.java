package com.codeless.api.automation.service.security.impl;

import com.codeless.api.automation.entity.security.User;
import com.codeless.api.automation.entity.security.UserDto;
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

  public UserDto saveUser(UserDto userDto) {
    userRepository.save(encodeUser(userDto));
    log.info("User with name: '{}' created successfully", userDto.getUsername());
    return userDto;
  }

  private User encodeUser(UserDto userDto) {
    String password = passwordEncoder.encode(userDto.getPassword());
    return new User()
        .setUsername(userDto.getUsername())
        .setPassword(password)
        .setRole(UserRole.ROLE_USER)
        .setEnabled(true)
        .setAccountNonExpired(true)
        .setCredentialsNonExpired(true)
        .setAccountNonLocked(true);
  }
}
