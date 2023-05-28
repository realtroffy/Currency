package com.idf.currency.service.impl;

import com.idf.currency.exception.BadRequestException;
import com.idf.currency.exception.ServerUnavailableException;
import com.idf.currency.model.Currency;
import com.idf.currency.service.WebClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static reactor.core.publisher.Mono.error;

@Service
public class WebClientCoinLoreimpl implements WebClientService<Currency> {

  private static final String SERVER_UNAVAILABLE_EXCEPTION_MESSAGE =
      "Server CoinLore.com is not responding";

  private final String currencyUrl;
  private final WebClient webClient;

  public WebClientCoinLoreimpl(
      @Value("${coinlore.url.currencyUrl}") String currencyUrl, WebClient webClient) {
    this.currencyUrl = currencyUrl;
    this.webClient = webClient;
  }

  @Override
  public Flux<Currency> getResponse() {
    return webClient
        .get()
        .uri(currencyUrl)
        .retrieve()
        .onStatus(
            HttpStatus::is4xxClientError,
            error -> error(new BadRequestException("Bad request for url: " + currencyUrl)))
        .onStatus(
            HttpStatus::is5xxServerError,
            error -> error(new ServerUnavailableException(SERVER_UNAVAILABLE_EXCEPTION_MESSAGE)))
        .bodyToFlux(Currency.class)
        .timeout(Duration.ofMinutes(3));
  }
}
