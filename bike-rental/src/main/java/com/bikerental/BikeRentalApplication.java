package com.bikerental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.bikerental.model")
@EnableJpaRepositories("com.bikerental.repository")
public class BikeRentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(BikeRentalApplication.class, args);
	}
}