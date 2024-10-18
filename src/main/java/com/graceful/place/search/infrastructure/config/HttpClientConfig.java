package com.graceful.place.search.infrastructure.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.graceful.place.search.infrastructure.interceptor.HttpClientLoggingInterceptor;

@Configuration
public class HttpClientConfig {

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Collections.singletonList(new HttpClientLoggingInterceptor()));
		return restTemplate;
	}
}
