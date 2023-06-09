package com.idf.currency.service;

import com.idf.currency.model.User;
import com.idf.currency.model.dto.UserDto;

import java.util.List;

public interface UserService {
  User save(UserDto userDto);

  User findByUsername(String username);

  User update(User user);

  List<User> getNotNotifiedUserFromDB();
}
