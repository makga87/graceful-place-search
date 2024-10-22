package com.graceful.place.search.infrastructure.config;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpClientConfig {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

		factory.setConnectTimeout(2000);
		factory.setReadTimeout(3000);
		factory.setConnectionRequestTimeout(3000);
		factory.setBufferRequestBody(false);
		factory.setHttpClient(HttpClientBuilder.create()
											   .setMaxConnTotal(50)
											   .setMaxConnPerRoute(25)
											   .build());
		return builder.requestFactory(() -> factory)
					  .build();
	}

}
