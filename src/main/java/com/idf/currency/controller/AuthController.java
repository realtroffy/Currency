package com.idf.currency.controller;

import com.idf.currency.config.security.jwt.JwtProvider;
import com.idf.currency.model.dto.UserDto;
import com.idf.currency.service.EmailService;
import com.idf.currency.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AuthController {

  private final UserService userService;
  private final EmailService emailService;
  private final JwtProvider jwtProvider;

  @PostMapping("/generate_token")
  public ResponseEntity<Map<Object, Object>> generateToken(@Valid @RequestBody UserDto userDto) {
    Map<Object, Object> response = new HashMap<>();
    String token = jwtProvider.generateToken(userDto.getUsername());
    response.put("username: ", userService.save(userDto).getUsername());
    response.put("token: ", token);
    emailService.sendEmail(
        userDto.getEmail(),
        "Registration on currency service",
        "Your name is: " + userDto.getUsername() + "Your token is: " + token);
    return ResponseEntity.status(CREATED).body(response);
  }
}
