package com.idf.currency.service.impl;

import com.idf.currency.model.Currency;
import com.idf.currency.model.User;
import com.idf.currency.repository.CurrencyRepository;
import com.idf.currency.service.NotifyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class NotifyServiceImpl implements NotifyService {

  public static final Set<User> USER_NOTIFY_SET = ConcurrentHashMap.newKeySet();
  public static final String NO_SUCH_ELEMENT_EXCEPTION_MESSAGE = "Currency not found";
  private final CurrencyRepository currencyRepository;

  @Override
  public void addToNotifyList(String username, String symbol) {
    Currency currency =
        currencyRepository
            .findBySymbol(symbol)
            .orElseThrow(() -> new NoSuchElementException(NO_SUCH_ELEMENT_EXCEPTION_MESSAGE));
    User user =
        USER_NOTIFY_SET.stream()
            .filter(usr -> usr.getUsername().equals(username))
            .findFirst()
            .orElse(createUser(username, currency));
    user.getCurrencyNotifyMap().put(currency, false);
    USER_NOTIFY_SET.add(user);
  }

  private User createUser(String username, Currency currency) {
    Map<Currency, Boolean> currencyMap = new HashMap<>();
    currencyMap.put(currency, false);
    return new User(username, currencyMap);
  }
}
