package com.codeless.api.automation.security.domain;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.Collection;
import java.util.Collections;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Data
@Accessors(chain = true)
@JsonView(View.ALLOWED.class)
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "username")
  @NotEmpty
  @Email
  private String username;

  @Size(min = 5)
  @NotEmpty
  @Column(name = "password")
  @JsonView(View.NOT_ALLOWED.class)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  @JsonView(View.NOT_ALLOWED.class)
  private UserRole role;

  @Column(name = "is_account_non_expired")
  private boolean isAccountNonExpired;

  @Column(name = "is_account_non_locked")
  private boolean isAccountNonLocked;

  @Column(name = "is_credentials_non_expired")
  private boolean isCredentialsNonExpired;

  @Column(name = "enabled")
  private boolean isEnabled;

  @Override
  @JsonView(View.NOT_ALLOWED.class)
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
  }
}
