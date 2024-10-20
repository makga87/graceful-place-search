package com.graceful.place.search.adapter.out.api.naver;


import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.graceful.place.search.application.port.out.PlaceSearchPort;
import com.graceful.place.search.infrastructure.config.ApiProperties;
import com.graceful.place.search.infrastructure.external.HttpClient;


@RequiredArgsConstructor
@Component
public class NaverPlaceSearchAdapter implements PlaceSearchPort<NaverPlaceSearchApiRequest, NaverPlaceSearchApiResponse> {

	private final HttpClient httpClient;

	@Override
	public NaverPlaceSearchApiResponse searchPlaces(NaverPlaceSearchApiRequest request) {

		Map<String, Object> params = new HashMap<>();
		params.put("query", request.getQuery());
		params.put("display", request.getDisplay());

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Naver-Client-Id", ApiProperties.NAVER_CLIENT_ID);
		headers.set("X-Naver-Client-Secret", ApiProperties.NAVER_CLIENT_SECRET);
		headers.setContentType(MediaType.APPLICATION_JSON);

		return httpClient.get(ApiProperties.NAVER_URL, params, headers, NaverPlaceSearchApiResponse.class);
	}
}
