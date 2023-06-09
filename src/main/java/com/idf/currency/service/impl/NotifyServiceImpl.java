package com.idf.currency.service.impl;

import com.idf.currency.model.Currency;
import com.idf.currency.model.User;
import com.idf.currency.service.CurrencyService;
import com.idf.currency.service.NotifyService;
import com.idf.currency.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.idf.currency.service.impl.CurrencyServiceImpl.ACTUAL_CURRENCY_MAP;

@Service
@AllArgsConstructor
public class NotifyServiceImpl implements NotifyService {

  public static final Set<User> USER_NOTIFY_SET = ConcurrentHashMap.newKeySet();

  private final CurrencyService currencyService;
  private final UserService userService;

  @PostConstruct
  public void getNotNotifiedUserFromDB() {
    USER_NOTIFY_SET.addAll(userService.getNotNotifiedUserFromDB());
  }

  @PostConstruct
  public void updateCurrency() {
    currencyService.saveCurrencySync();
  }

  @Override
  @Transactional
  public void addToNotifyList(String username, String symbol) {
    Currency currency = getCurrencyFromActualSet(symbol);
    User user =
        USER_NOTIFY_SET.stream()
            .filter(usr -> usr.getUsername().equals(username))
            .findFirst()
            .orElseGet(() -> userService.findByUsername(username));

    if (user.getCurrencyNotifyList().stream()
        .noneMatch(e -> e.getSymbol().equals(currency.getSymbol()))) {
      user.getCurrencyNotifyList().add(currency);
    } else {
      user.getCurrencyNotifyList()
          .replaceAll(
              e -> {
                if (e.getSymbol().equals(currency.getSymbol())) {
                  e.setPriceUsd(currency.getPriceUsd());
                  e.setTime(currency.getTime());
                }
                return e;
              });
    }
    USER_NOTIFY_SET.add(user);
    userService.update(user);
  }

  private Currency getCurrencyFromActualSet(String symbol) {
    return ACTUAL_CURRENCY_MAP.values().stream()
        .filter(currency -> currency.getSymbol().equals(symbol))
        .findFirst()
        .orElseGet(() -> getCurrencyFromDB(symbol));
  }


  private Currency getCurrencyFromDB(String symbol) {
    return currencyService.getCurrencyBySymbol(symbol);
  }
}
