package com.graceful.place.search.application.mapper;


import org.jsoup.Jsoup;

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
					.placeName(Jsoup.parse(item.getTitle()).text())
					.address(item.getAddress())
					.roadAddress(item.getRoadAddress())
					.x(item.getMapx())
					.y(item.getMapy())
					.searchApiType(SearchApiType.NAVER)
					.build();
	}
}
