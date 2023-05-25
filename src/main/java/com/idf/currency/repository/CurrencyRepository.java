package com.idf.currency.repository;

import com.idf.currency.model.Currency;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CurrencyRepository extends CrudRepository<Currency, Long> {

    Optional<Currency> findBySymbol(String symbol);
}
