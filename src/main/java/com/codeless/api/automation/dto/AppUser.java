package com.codeless.api.automation.dto;

import com.fasterxml.jackson.annotation.JsonView;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonView(View.ALLOWED.class)
public class AppUser {

  @NotEmpty
  @Email
  private String username;

  @Size(max = 100, min = 5, message = "Invalid password size. Min - 5, Max - 100.")
  @NotEmpty
  @JsonView(View.NOT_ALLOWED.class)
  private String password;
}
