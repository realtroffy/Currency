package com.idf.currency.exception;

import java.util.NoSuchElementException;

public class NotFoundCurrencyException extends NoSuchElementException {

  public NotFoundCurrencyException(String s) {
    super(s);
  }
}
