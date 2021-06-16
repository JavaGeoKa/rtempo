package com.geo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class RTempoApplication {

	public static void main(String[] args) {

		ApiContextInitializer.init();
		SpringApplication.run(RTempoApplication.class, args);
	}

}
