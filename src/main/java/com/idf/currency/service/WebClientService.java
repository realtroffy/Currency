package com.idf.currency.service;

import org.springframework.http.ResponseEntity;

public interface WebClientService<T> {
  ResponseEntity<T> getResponseEntity(String url);
}
