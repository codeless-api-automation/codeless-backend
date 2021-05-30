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
  private String firstName;

  @NotEmpty
  private String lastName;

  @NotEmpty
  @Email
  private String email;

  @Size(max = 100, min = 5, message = "The email '${validatedValue}' must be between {min} and {max} characters long.")
  @NotEmpty
  private String password;
}
