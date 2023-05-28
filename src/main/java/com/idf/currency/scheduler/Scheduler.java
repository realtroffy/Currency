package com.idf.currency.scheduler;

import com.idf.currency.model.Currency;
import com.idf.currency.model.User;
import com.idf.currency.service.CurrencyService;
import com.idf.currency.service.impl.CurrencyServiceImpl;
import com.idf.currency.service.impl.NotifyServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@AllArgsConstructor
public class Scheduler {

  private final CurrencyService currencyService;

  @Scheduled(cron = "0 * * * * ?")
  public void updateCurrencyFromSource() {
    CurrencyServiceImpl.ACTUAL_CURRENCY_SET.clear();
    currencyService.saveCurrency();
  }

  @Scheduled(cron = "1 * * * * ?")
  public void notifyUser() {
    log.info("Start notify users");
    for (User usr : NotifyServiceImpl.USER_NOTIFY_SET) {
      for (Map.Entry<Currency, Boolean> entry : usr.getCurrencyNotifyMap().entrySet()) {
        for (Currency actualCurrency : CurrencyServiceImpl.ACTUAL_CURRENCY_SET) {
          double percentDif = actualCurrency.getPriceUsd() / entry.getKey().getPriceUsd();
          if (Boolean.TRUE.equals(
                  !entry.getValue() && actualCurrency.getId().equals(entry.getKey().getId()))
              && (percentDif >= 1.01 || percentDif <= 0.99)) {
            log.warn(
                usr.getUsername()
                    + " "
                    + entry.getKey().getSymbol()
                    + " "
                    + new DecimalFormat("#.###").format(percentDif * 100 - 100));
            entry.setValue(true);
          }
        }
      }
    }
  }

  @Scheduled(cron = "5 0 * * * ?")
  public void removeNotifiedUsersFromSet() {
    log.info("Start remove notified users set");
    Set<User> notNotifiedSet = ConcurrentHashMap.newKeySet();
    for (User usr : NotifyServiceImpl.USER_NOTIFY_SET) {
      for (Map.Entry<Currency, Boolean> entry : usr.getCurrencyNotifyMap().entrySet()) {
        if (Boolean.FALSE.equals(entry.getValue())) {
          notNotifiedSet.add(usr);
        }
      }
    }
    NotifyServiceImpl.USER_NOTIFY_SET.removeIf(user -> !notNotifiedSet.contains(user));
  }
}
