package com.graceful.place.search.application.mapper;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.graceful.place.search.adapter.out.api.naver.NaverPlaceSearchApiResponse;
import com.graceful.place.search.domain.Place;
import com.graceful.place.search.domain.SearchApiType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NaverPlaceMapper implements PlaceMapper<NaverPlaceSearchApiResponse.Item> {

	public static NaverPlaceMapper init() {
		return new NaverPlaceMapper();
	}

	@Override
	public Place toPlace(NaverPlaceSearchApiResponse.Item item) {
		return Place.builder()
					.placeName(item.getTitle())
					.address(item.getAddress())
					.roadAddress(item.getRoadAddress())
					.x(Double.parseDouble(item.getMapx()))
					.y(Double.parseDouble(item.getMapy()))
					.searchApiType(SearchApiType.NAVER)
					.build();
	}
}
