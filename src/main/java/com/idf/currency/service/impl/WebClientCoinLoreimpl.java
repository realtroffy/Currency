package com.idf.currency.service.impl;

import com.idf.currency.exception.BadRequestException;
import com.idf.currency.exception.ServerUnavailableException;
import com.idf.currency.service.WebClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

import static reactor.core.publisher.Mono.error;

@Service
@AllArgsConstructor
public class WebClientCoinLoreimpl implements WebClientService<Object[]> {

  private static final String SERVER_UNAVAILABLE_EXCEPTION_MESSAGE =
      "Server CoinLore.com is not responding";
  private final WebClient webClient;

  @Override
  public ResponseEntity<Object[]> getResponseEntity(String url) {
    return webClient
        .get()
        .uri(url)
        .retrieve()
        .onStatus(
            HttpStatus::is4xxClientError,
            error -> error(new BadRequestException("Bad request for url: " + url)))
        .onStatus(
            HttpStatus::is5xxServerError,
            error -> error(new ServerUnavailableException(SERVER_UNAVAILABLE_EXCEPTION_MESSAGE)))
        .toEntity(Object[].class)
        .timeout(Duration.ofMinutes(3))
        .block();
  }
}
