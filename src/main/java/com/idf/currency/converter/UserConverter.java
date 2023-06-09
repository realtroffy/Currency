package com.idf.currency.converter;

import com.idf.currency.model.User;
import com.idf.currency.model.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArrayList;

@Component
@AllArgsConstructor
public class UserConverter {

  private final PasswordEncoder encoder;

  public User convertUserDtoToUser(UserDto userDto) {
    User user = new User();
    user.setUsername(userDto.getUsername());
    user.setPassword(encoder.encode(userDto.getPassword()));
    user.setEmail(userDto.getEmail());
    user.setCurrencyNotifyList(new CopyOnWriteArrayList<>());
    return user;
  }
}
