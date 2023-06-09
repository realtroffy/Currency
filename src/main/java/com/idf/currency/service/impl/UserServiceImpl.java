package com.idf.currency.service.impl;

import com.idf.currency.converter.UserConverter;
import com.idf.currency.exception.AllReadyRegisteredUserException;
import com.idf.currency.model.User;
import com.idf.currency.model.dto.UserDto;
import com.idf.currency.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final MongoTemplate mongoTemplate;
  private final UserConverter userConverter;

  @Override
  public User save(UserDto userDto) {
    User user = findByUsername(userDto.getUsername());
    if (user == null) {
      return mongoTemplate.save(userConverter.convertUserDtoToUser(userDto));
    } else {
      throw new AllReadyRegisteredUserException(
          "User with name: " + userDto.getUsername() + " was already registered");
    }
  }

  @Override
  public User findByUsername(String username) {
    Query query = new Query();
    query.addCriteria(Criteria.where("username").is(username));
    return mongoTemplate.findOne(query, User.class);
  }

  @Override
  public User update(User user) {
    return mongoTemplate.save(user);
  }

  @Override
  public List<User> getNotNotifiedUserFromDB() {
    Query query = new Query();
    query.addCriteria(Criteria.where("currencyNotifyList").ne(Collections.emptyList()));
    return mongoTemplate.find(query, User.class);
  }
}
