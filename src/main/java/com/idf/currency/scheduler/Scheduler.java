package com.idf.currency.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idf.currency.exception.BodyNullException;
import com.idf.currency.model.Currency;
import com.idf.currency.model.User;
import com.idf.currency.repository.CurrencyRepository;
import com.idf.currency.service.WebClientService;
import com.idf.currency.service.impl.NotifyServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class Scheduler {

  public static final String BODY_NULL_EXCEPTION_MESSAGE = "Array of currencies is null";
  public static final Set<Currency> ACTUAL_CURRENCY_SET = new HashSet<>();

  private final String currencyUrl;
  private final WebClientService<?> webClientService;
  private final CurrencyRepository repository;

  @Autowired
  public Scheduler(
      @Value("${coinlore.url.currencyUrl}") String currencyUrl,
      WebClientService<?> webClientService,
      CurrencyRepository repository) {
    this.currencyUrl = currencyUrl;
    this.webClientService = webClientService;
    this.repository = repository;
  }

  @Scheduled(cron = "0 * * * * ?")
  @PostConstruct
  public void updateCurrency() {
    ACTUAL_CURRENCY_SET.clear();
    ObjectMapper mapper = new ObjectMapper();
    Object[] currencies = (Object[]) webClientService.getResponseEntity(currencyUrl).getBody();
    if (currencies == null || currencies.length == 0) {
      throw new BodyNullException(BODY_NULL_EXCEPTION_MESSAGE);
    }
    Arrays.stream(currencies)
        .map(object -> mapper.convertValue(object, Currency.class))
        .forEach(
            entity -> {
              repository.save(entity);
              ACTUAL_CURRENCY_SET.add(entity);
            });
  }

  @Scheduled(cron = "1 * * * * ?")
  public void notifyUser() {
    log.info("Start notify users");
    for (User usr : NotifyServiceImpl.USER_NOTIFY_SET) {
      for (Map.Entry<Currency, Boolean> entry : usr.getCurrencyNotifyMap().entrySet()) {
        for (Currency actualCurrency : ACTUAL_CURRENCY_SET) {
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
