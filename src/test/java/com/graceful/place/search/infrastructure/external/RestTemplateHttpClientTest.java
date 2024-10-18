package com.graceful.place.search.infrastructure.external;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

import com.graceful.place.search.infrastructure.config.ApiProperties;


@Slf4j
class RestTemplateHttpClientTest {

	@Test
	void testGet() {

		RestTemplate restTemplate = new RestTemplate();

		String keyword = "은행";

		Map<String, String> params = new HashMap<>();
		params.put("query", keyword);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", ApiProperties.KAKAO_AUTH);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<?> entity = new HttpEntity<>(headers);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ApiProperties.KAKAO_URL);
		params.forEach(builder::queryParam);

		ResponseEntity<String> response =
				restTemplate.exchange(builder.buildAndExpand(params).toUriString(), HttpMethod.GET, entity, String.class);

		assertEquals(200, response.getStatusCodeValue());
		assertTrue(StringUtils.isNotBlank(response.getBody()));
	}
}