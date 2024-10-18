package com.graceful.place.search.adapter.out.api.naver;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.graceful.place.search.adapter.out.api.PlaceSearchApiRequest;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NaverPlaceSearchApiRequest implements PlaceSearchApiRequest {
	private String query;
	private Integer display;

	public static NaverPlaceSearchApiRequest of(String query, Integer display) {
		return new NaverPlaceSearchApiRequest(query, display);
	}

}
