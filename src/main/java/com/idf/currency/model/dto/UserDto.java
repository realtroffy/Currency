package com.idf.currency.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class UserDto {

  public static final String BAD_USER_NAME_MESSAGE =
      "Username length must be between 3 and 20 characters";
  public static final String BAD_PASSWORD_MESSAGE =
      "Username length must be between 6 and 25 characters";

  @Size(min = 3, max = 25, message = BAD_USER_NAME_MESSAGE)
  private String username;

  @Email private String email;

  @Size(min = 3, max = 25, message = BAD_PASSWORD_MESSAGE)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
}
