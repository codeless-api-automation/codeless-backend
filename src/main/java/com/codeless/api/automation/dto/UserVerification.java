package com.codeless.api.automation.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserVerification {

  @NotEmpty
  private String uuid;

  @NotEmpty
  private String date;

  @NotEmpty
  @Email
  private String email;
}
