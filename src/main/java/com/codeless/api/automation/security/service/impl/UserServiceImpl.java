package com.codeless.api.automation.security.service.impl;

import com.codeless.api.automation.security.domain.User;
import com.codeless.api.automation.security.domain.UserRole;
import com.codeless.api.automation.security.repository.UserRepository;
import com.codeless.api.automation.security.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDetails user = userRepository.findByUsername(username);
        if (Objects.nonNull(user)) {
            logger.info("Find user with name: '{}'", username);
            return user;
        }
        throw new UsernameNotFoundException("User not exist with name :" + username);
    }

    public User saveUser(User user) {
        return userRepository.save(encodeUser(user));
    }

    private User encodeUser(User user) {
        String password = passwordEncoder.encode(user.getPassword());
        return user.setPassword(password)
                .setRole(UserRole.ROLE_USER)
                .setEnabled(true)
                .setAccountNonExpired(true)
                .setCredentialsNonExpired(true)
                .setAccountNonLocked(true);
    }
}
