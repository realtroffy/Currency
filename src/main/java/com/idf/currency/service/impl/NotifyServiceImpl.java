package com.idf.currency.service.impl;

import com.idf.currency.exception.NotFoundCurrencyException;
import com.idf.currency.model.Currency;
import com.idf.currency.model.User;
import com.idf.currency.repository.CurrencyRepository;
import com.idf.currency.service.CurrencyService;
import com.idf.currency.service.NotifyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.idf.currency.service.impl.CurrencyServiceImpl.ACTUAL_CURRENCY_MAP;

@Service
@AllArgsConstructor
public class NotifyServiceImpl implements NotifyService {

  public static final Set<User> USER_NOTIFY_SET = ConcurrentHashMap.newKeySet();
  public static final String NOT_FOUND_ELEMENT_EXCEPTION_MESSAGE = "Currency not found";

  private final CurrencyRepository currencyRepository;
  private final CurrencyService currencyService;

  @Override
  public void addToNotifyList(String username, String symbol) {
    Currency currency = getCurrencyFromActualSet(symbol);
    User user =
        USER_NOTIFY_SET.stream()
            .filter(usr -> usr.getUsername().equals(username))
            .findFirst()
            .orElse(createUser(username, currency));
    user.getCurrencyNotifyMap().put(currency, false);
    USER_NOTIFY_SET.add(user);
  }

  private Currency getCurrencyFromActualSet(String symbol) {
    return ACTUAL_CURRENCY_MAP.values().stream()
        .filter(currency -> currency.getSymbol().equals(symbol))
        .findFirst()
        .orElseGet(() -> getCurrencyFromDB(symbol));
  }

  private Currency getCurrencyFromDB(String symbol) {
    return currencyRepository.findBySymbol(symbol).orElseGet(() -> getCurrencyFromUrl(symbol));
  }

  private Currency getCurrencyFromUrl(String symbol) {
    currencyService.saveCurrencySync();
    return ACTUAL_CURRENCY_MAP.values().stream()
        .filter(currency -> filterCurrencyBySymbol(currency, symbol))
        .findFirst()
        .orElseThrow(() -> new NotFoundCurrencyException(NOT_FOUND_ELEMENT_EXCEPTION_MESSAGE));
  }

  private boolean filterCurrencyBySymbol(Currency currency, String symbol) {
    return currency.getSymbol().equals(symbol);
  }

  private User createUser(String username, Currency currency) {
    Map<Currency, Boolean> currencyMap = new ConcurrentHashMap<>();
    currencyMap.put(currency, false);
    return new User(username, currencyMap);
  }
}
