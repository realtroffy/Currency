package com.idf.currency.service.impl;

import com.idf.currency.service.ValidationService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Service
public class ValidationServiceImpl implements ValidationService {

  public static final Set<String> currencyNameSet = new HashSet<>();

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
}
