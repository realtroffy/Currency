package com.idf.currency.service;

public interface EmailService {

  void sendEmail(String to, String subject, String text);
}
