package com.graceful.place.search.adapter.in.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlaceSearchResponse {

	private String placeName;

	public static PlaceSearchResponse from(String placeName) {
		return new PlaceSearchResponse(placeName);
	}
}
