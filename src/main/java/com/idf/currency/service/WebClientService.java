package com.idf.currency.service;

import reactor.core.publisher.Flux;

public interface WebClientService<T> {
  Flux<T> getResponse();
}
