package com.cob3218.metroroulette;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class MetrorouletteApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetrorouletteApplication.class, args);
	}

}
