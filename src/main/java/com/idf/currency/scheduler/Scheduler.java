package com.idf.currency.scheduler;

import com.idf.currency.model.Currency;
import com.idf.currency.model.User;
import com.idf.currency.service.CurrencyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.idf.currency.service.impl.CurrencyServiceImpl.ACTUAL_CURRENCY_MAP;
import static com.idf.currency.service.impl.NotifyServiceImpl.USER_NOTIFY_SET;

@Component
@Slf4j
@AllArgsConstructor
public class Scheduler {

  private final CurrencyService currencyService;

  @Scheduled(cron = "0 * * * * ?")
  public void updateCurrencyFromSource() {
    currencyService.saveCurrencyAsync();
  }

  @Scheduled(cron = "1 * * * * ?")
  public void notifyUser() {
    Set<User> notifiedAlreadySet = new HashSet<>();
    for (User usr : USER_NOTIFY_SET) {
      for (Map.Entry<Currency, Boolean> entry : usr.getCurrencyNotifyMap().entrySet()) {
        String currencySymbol = entry.getKey().getSymbol();
        Currency currentCurrency = ACTUAL_CURRENCY_MAP.get(currencySymbol);
        if (ACTUAL_CURRENCY_MAP.containsKey(currencySymbol)) {
          double percentDif = currentCurrency.getPriceUsd() / entry.getKey().getPriceUsd();
          if (percentDif >= 1.01 || percentDif <= 0.99) {
            log.warn(
                usr.getUsername()
                    + " "
                    + entry.getKey().getSymbol()
                    + " "
                    + new DecimalFormat("#.###").format(percentDif * 100 - 100));
            usr.getCurrencyNotifyMap().remove(entry.getKey());
          }
        }
      }
      if (usr.getCurrencyNotifyMap().isEmpty()) {
        notifiedAlreadySet.add(usr);
      }
      USER_NOTIFY_SET.removeAll(notifiedAlreadySet);
    }
  }
}
