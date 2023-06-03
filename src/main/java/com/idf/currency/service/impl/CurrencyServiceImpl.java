package com.idf.currency.service.impl;

import com.idf.currency.exception.BodyNullException;
import com.idf.currency.model.Currency;
import com.idf.currency.service.CurrencyService;
import com.idf.currency.service.WebClientService;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

  public static final String BODY_NULL_EXCEPTION_MESSAGE = "Array of currencies is null";
  public static final Map<String, Currency> ACTUAL_CURRENCY_MAP = new ConcurrentHashMap<>();

  private final MongoTemplate mongoTemplate;
  private final WebClientService<?> webClientService;

  @Override
  @Transactional
  public void saveCurrencyAsync() {
    webClientService.getResponse().subscribe(currency -> save((Currency) currency));
  }

  @Override
  @Transactional
  public void saveCurrencySync() {
    List<Currency> currenciesFromUrl =
        (List<Currency>) webClientService.getResponse().collectList().block();
    if (currenciesFromUrl == null) {
      throw new BodyNullException(BODY_NULL_EXCEPTION_MESSAGE);
    } else {
      currenciesFromUrl.forEach(this::save);
    }
  }

  private void save(Currency currency) {
    if (currency == null) {
      throw new BodyNullException(BODY_NULL_EXCEPTION_MESSAGE);
    } else {
      currency.setTime(LocalDateTime.now());
      mongoTemplate.save(currency);
      ACTUAL_CURRENCY_MAP.put(currency.getSymbol(), currency);
    }
  }
}
