package com.graceful.place.search.adapter.out.api.kakao;


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
public class KakaoPlaceSearchAdapter implements PlaceSearchPort<KakaoPlaceSearchApiRequest, KakaoPlaceSearchApiResponse> {

	private final PlaceSearchConfig config;
	private final HttpClient httpClient;

	@Override
	public KakaoPlaceSearchApiResponse searchPlaces(KakaoPlaceSearchApiRequest request) {
		Map<String, Object> params = new HashMap<>();
		params.put("query", request.getKeyword());

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", config.getApi().getKakao().getKey());
		headers.setContentType(MediaType.APPLICATION_JSON);

		return httpClient.get(config.getApi().getKakao().getUrl(), params, headers, KakaoPlaceSearchApiResponse.class);
	}
}
