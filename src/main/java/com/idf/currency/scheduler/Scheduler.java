package com.idf.currency.scheduler;

import com.idf.currency.model.Currency;
import com.idf.currency.model.User;
import com.idf.currency.service.CurrencyService;
import com.idf.currency.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.idf.currency.service.impl.CurrencyServiceImpl.ACTUAL_CURRENCY_MAP;
import static com.idf.currency.service.impl.NotifyServiceImpl.USER_NOTIFY_SET;

@Component
@Slf4j
@AllArgsConstructor
public class Scheduler {

  private final CurrencyService currencyService;
  private final UserService userService;

  @Scheduled(cron = "0 * * * * ?")
  public void updateCurrencyFromSource() {
    currencyService.saveCurrencyAsync();
  }

  @Scheduled(cron = "1 * * * * ?")
  public void notifyUser() {
    List<User> notifiedAlreadyList = new ArrayList<>();
    for (User usr : USER_NOTIFY_SET) {
      Iterator<Currency> iterator = usr.getCurrencyNotifyList().listIterator();
      while (iterator.hasNext()) {
        Currency next = iterator.next();
        String currencySymbol = next.getSymbol();
        Currency currentCurrency = ACTUAL_CURRENCY_MAP.get(currencySymbol);
        if (ACTUAL_CURRENCY_MAP.containsKey(currencySymbol)) {
          double percentDif = currentCurrency.getPriceUsd() / next.getPriceUsd();
          if (percentDif >= 1.01 || percentDif <= 0.99) {
            log.warn(
                usr.getUsername()
                    + " "
                    + next.getSymbol()
                    + " "
                    + new DecimalFormat("#.###").format(percentDif * 100 - 100));
            iterator.remove();
          }
        }
      }
      if (usr.getCurrencyNotifyList().isEmpty()) {
        notifiedAlreadyList.add(usr);
      }
      USER_NOTIFY_SET.forEach(userService::update);
      USER_NOTIFY_SET.removeAll(notifiedAlreadyList);
    }
  }
}
