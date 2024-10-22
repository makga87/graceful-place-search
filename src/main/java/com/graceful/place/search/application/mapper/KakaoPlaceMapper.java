package com.graceful.place.search.application.mapper;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.graceful.place.search.adapter.out.api.kakao.KakaoPlaceSearchApiResponse;
import com.graceful.place.search.domain.Place;
import com.graceful.place.search.domain.SearchApiType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoPlaceMapper implements PlaceMapper<KakaoPlaceSearchApiResponse.Document> {

	public static KakaoPlaceMapper init() {
		return new KakaoPlaceMapper();
	}

	@Override
	public Place toPlace(KakaoPlaceSearchApiResponse.Document document) {
		return Place.builder()
					.placeName(document.getPlaceName())
					.address(document.getAddressName())
					.roadAddress(document.getRoadAddressName())
					.x(document.getX())
					.y(document.getY())
					.priorityScore(SearchApiType.KAKAO.getAdditionalScore())
					.searchApiType(SearchApiType.KAKAO)
					.build();
	}
}
