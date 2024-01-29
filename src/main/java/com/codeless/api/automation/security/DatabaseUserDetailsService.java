package com.codeless.api.automation.security;

import com.codeless.api.automation.entity.User;
import com.codeless.api.automation.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class DatabaseUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public DatabaseUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.get(username);
    if (user == null) {
      throw new UsernameNotFoundException("User with username [" + username + "] not found.");
    }
    return new CustomUserDetails(user);
  }
}
