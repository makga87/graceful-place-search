package com.graceful.place.search.infrastructure.external;


import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RestTemplateHttpClient implements HttpClient {

	private final RestTemplate restTemplate;

	@Override
	public <R> R get(String url, Map<String, Object> params, HttpHeaders headers, Class<R> returnType) {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		params.forEach(builder::queryParam);

		HttpEntity<?> entity = new HttpEntity<>(headers);

		return restTemplate.exchange(builder.buildAndExpand(params).toUriString(), HttpMethod.GET, entity, returnType).getBody();

	}
}
