package com.graceful.place.search.adapter.out.api.naver;


import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.graceful.place.search.application.port.out.PlaceSearchPort;
import com.graceful.place.search.config.PlaceSearchConfig;
import com.graceful.place.search.infrastructure.external.HttpClient;


@RequiredArgsConstructor
@Component
public class NaverPlaceSearchAdapter implements PlaceSearchPort<NaverPlaceSearchApiRequest, NaverPlaceSearchApiResponse> {

	private final PlaceSearchConfig config;
	private final HttpClient httpClient;

	@Override
	public NaverPlaceSearchApiResponse searchPlaces(NaverPlaceSearchApiRequest request) {

		Map<String, Object> params = new HashMap<>();
		params.put("query", request.getQuery());
		params.put("display", request.getDisplay());

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Naver-Client-Id", config.getApi().getNaver().getId());
		headers.set("X-Naver-Client-Secret", config.getApi().getNaver().getSecret());
		headers.setContentType(MediaType.APPLICATION_JSON);

		return httpClient.get(config.getApi().getNaver().getUrl(), params, headers, NaverPlaceSearchApiResponse.class);
	}
}
