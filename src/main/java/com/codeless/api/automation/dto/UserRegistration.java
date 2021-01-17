package com.codeless.api.automation.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRegistration {

  @NotEmpty
  @Email
  private String username;

  @Size(max = 100, min = 5, message = "Invalid password size. Min - 5, Max - 100.")
  @NotEmpty
  private String password;
}
