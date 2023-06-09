package com.idf.currency.service.impl;

import com.idf.currency.config.security.jwt.JwtProvider;
import com.idf.currency.service.ValidationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class ValidationServiceImpl implements ValidationService {

  public static final Set<String> currencyNameSet = new HashSet<>();

  private final JwtProvider jwtProvider;

  @PostConstruct
  public void fillCurrencyNameSet() {
    currencyNameSet.add("BTC");
    currencyNameSet.add("ETH");
    currencyNameSet.add("SOL");
  }

  @Override
  public boolean isValidCurrencyName(String currency) {
    return currencyNameSet.contains(currency);
  }

  @Override
  public boolean isTokenConsistUsernameFromUrl(String username, String authorizationHeader) {
    String token = authorizationHeader.substring(7);
    return username.equals(jwtProvider.getUsernameFromToken(token));
  }
}
