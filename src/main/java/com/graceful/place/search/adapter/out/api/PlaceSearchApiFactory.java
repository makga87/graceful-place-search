package com.graceful.place.search.adapter.out.api;


import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.graceful.place.search.application.port.out.PlaceSearchPort;
import com.graceful.place.search.domain.SearchApiType;

@SuppressWarnings("rawtypes")
@Component
public class PlaceSearchApiFactory {

	private final Map<SearchApiType, PlaceSearchPort<PlaceSearchApiRequest, PlaceSearchApiResponse<?>>> portMap;

	@SuppressWarnings("unchecked")
	public PlaceSearchApiFactory(@Qualifier("kakaoPlaceSearchAdapter") PlaceSearchPort kakaoPlaceSearchAdapter,
								 @Qualifier("naverPlaceSearchAdapter") PlaceSearchPort naverPlaceSearchAdapter) {

		this.portMap = Map.of(SearchApiType.KAKAO, kakaoPlaceSearchAdapter,
							  SearchApiType.NAVER, naverPlaceSearchAdapter);
	}

	public PlaceSearchPort getSearchApi(SearchApiType searchApiType) {
		return portMap.get(searchApiType);
	}
}
