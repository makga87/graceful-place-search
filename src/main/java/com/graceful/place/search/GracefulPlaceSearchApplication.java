package com.graceful.place.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class GracefulPlaceSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(GracefulPlaceSearchApplication.class, args);
	}

}
