package com.idf.currency.service.impl;

import com.idf.currency.exception.BodyNullException;
import com.idf.currency.model.Currency;
import com.idf.currency.repository.CurrencyRepository;
import com.idf.currency.service.CurrencyService;
import com.idf.currency.service.WebClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

  public static final String BODY_NULL_EXCEPTION_MESSAGE = "Array of currencies is null";
  public static final Map<String, Currency> ACTUAL_CURRENCY_MAP = new ConcurrentHashMap<>();

  private final CurrencyRepository repository;
  private final WebClientService<?> webClientService;

  @Override
  public void saveCurrencyAsync() {
    webClientService.getResponse().subscribe(currency -> save((Currency) currency));
  }

  @Override
  public void saveCurrencySync() {
    List<Currency> currenciesFromUrl =
        (List<Currency>) webClientService.getResponse().collectList().block();
    if (currenciesFromUrl == null) {
      throw new BodyNullException(BODY_NULL_EXCEPTION_MESSAGE);
    } else {
      currenciesFromUrl.forEach(e -> ACTUAL_CURRENCY_MAP.put(e.getSymbol(), e));
    }
  }

  private void save(Currency currency) {
    if (currency == null) {
      throw new BodyNullException(BODY_NULL_EXCEPTION_MESSAGE);
    } else {
      repository.save(currency);
      ACTUAL_CURRENCY_MAP.put(currency.getSymbol(), currency);
    }
  }
}
