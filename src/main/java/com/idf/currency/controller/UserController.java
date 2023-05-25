package com.idf.currency.controller;

import com.idf.currency.service.NotifyService;
import com.idf.currency.service.ValidationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Validated
public class UserController {

  private static final String BAD_SYMBOL_MESSAGE = "Bad request. Available currency: BTC, ETH, SOL";
  private static final String BAD_USER_NAME_MESSAGE =
      "Username length must be between 3 and 20 characters";
  private final ValidationService validationService;
  private final NotifyService notifyService;

  @GetMapping("/{username}/{symbol}")
  public ResponseEntity<String> notifyUser(
      @PathVariable @Size(min = 3, max = 20, message = BAD_USER_NAME_MESSAGE) String username,
      @Size(min = 3, message = BAD_SYMBOL_MESSAGE) @PathVariable String symbol) {
    if (!validationService.isValidCurrencyName(symbol)) {
      return new ResponseEntity<>(
          "No such currency: " + symbol + ". " + BAD_SYMBOL_MESSAGE, HttpStatus.BAD_REQUEST);
    }
    notifyService.addToNotifyList(username, symbol);
    return new ResponseEntity<>(
        "User with name "
            + username
            + " will be notify for change "
            + symbol
            + " price by 1 percent",
        HttpStatus.OK);
  }
}
