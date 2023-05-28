package com.idf.currency.service.impl;

import com.idf.currency.exception.BodyNullException;
import com.idf.currency.model.Currency;
import com.idf.currency.repository.CurrencyRepository;
import com.idf.currency.service.CurrencyService;
import com.idf.currency.service.WebClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    public static final String BODY_NULL_EXCEPTION_MESSAGE = "Array of currencies is null";
    public static final Set<Currency> ACTUAL_CURRENCY_SET = new HashSet<>();

    private final CurrencyRepository repository;
    private final WebClientService<?> webClientService;

    @Override
    public void saveCurrency() {
    webClientService.getResponse().subscribe(currency -> save((Currency) currency));
    }

    private void save(Currency currency) {
        if (currency == null) {
            throw new BodyNullException(BODY_NULL_EXCEPTION_MESSAGE);
        } else {
            repository.save(currency);
            ACTUAL_CURRENCY_SET.add(currency);
        }
    }
}
