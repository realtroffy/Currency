package com.idf.currency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CurrencyNotifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyNotifierApplication.class, args);
	}

}
