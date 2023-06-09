package com.idf.currency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class CurrencyNotifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyNotifierApplication.class, args);
	}

}
