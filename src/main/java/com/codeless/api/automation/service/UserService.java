package com.codeless.api.automation.service;

import com.codeless.api.automation.dto.UserRegistration;
import com.codeless.api.automation.entity.User;

public interface UserService {

  User saveUser(UserRegistration userRegistration);

  User getUserByEmail(String email);

  User verifyUser(String verificationToken);
}
