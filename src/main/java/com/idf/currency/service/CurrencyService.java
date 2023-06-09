package com.idf.currency.service;

import com.idf.currency.model.Currency;

public interface CurrencyService{

   void saveCurrencyAsync();

   void saveCurrencySync();

   Currency getCurrencyBySymbol(String symbol);
}
