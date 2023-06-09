package com.idf.currency.config.security;

import com.idf.currency.model.User;
import com.idf.currency.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  public static final String USERNAME_NOT_FOUND_EXCEPTION_MESSAGE = "User was not found exception";
  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userService.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException(USERNAME_NOT_FOUND_EXCEPTION_MESSAGE);
    }
    return CustomUserDetails.fromUserToCustomUserDetails(user);
  }
}
