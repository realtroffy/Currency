package com.idf.currency.exception.handler;

import com.idf.currency.exception.BadRequestException;
import com.idf.currency.exception.BodyNullException;
import com.idf.currency.exception.NotFoundCurrencyException;
import com.idf.currency.exception.ServerUnavailableException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({
    ConstraintViolationException.class,
    BadRequestException.class,
    ServerUnavailableException.class,
    BodyNullException.class,
    NotFoundCurrencyException.class
  })
  public ResponseEntity<Object> handleException(Exception ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }
}
