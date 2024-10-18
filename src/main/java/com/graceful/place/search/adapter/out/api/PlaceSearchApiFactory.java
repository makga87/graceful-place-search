package com.graceful.place.search.adapter.out.api;


import org.springframework.beans.factory.annotation.Qualifier;

import lombok.RequiredArgsConstructor;

import com.graceful.place.search.application.port.out.PlaceSearchPort;
import com.graceful.place.search.domain.SearchApiType;

@RequiredArgsConstructor
public class PlaceSearchApiFactory {

	@Qualifier("kakaoPlaceSearchAdapter")
	private final PlaceSearchPort kakaoPlaceSearchAdapter;

	@Qualifier("naverPlaceSearchAdapter")
	private final PlaceSearchPort naverPlaceSearchAdapter;


	public PlaceSearchPort getSearchApi(SearchApiType searchApiType) {

		switch (searchApiType) {
			case KAKAO:
				return kakaoPlaceSearchAdapter;
			case NAVER:
				return naverPlaceSearchAdapter;
			default:
				return null;
		}
	}
}
