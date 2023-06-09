package com.idf.currency.service;

public interface ValidationService {

  boolean isValidCurrencyName(String currency);

  boolean isTokenConsistUsernameFromUrl(String username, String authorizationHeader);
}
