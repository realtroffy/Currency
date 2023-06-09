package com.idf.currency.config.security;

import com.idf.currency.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

  private String username;
  private String password;
  private String email;

  public static CustomUserDetails fromUserToCustomUserDetails(User user) {
    CustomUserDetails details = new CustomUserDetails();
    details.username = user.getUsername();
    details.password = user.getPassword();
    details.email = user.getEmail();
    return details;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
