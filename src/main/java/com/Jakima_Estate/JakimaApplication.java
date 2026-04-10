package com.Jakima_Estate;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JakimaApplication {

	public static void main(String[] args) {
		SpringApplication.run(JakimaApplication.class, args);
	}

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@PostConstruct
	public void printDb() {
		System.out.println("DB URL: " + dbUrl);
	}

}
