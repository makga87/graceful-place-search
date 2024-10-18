package com.graceful.place.search.adapter.out.api.kakao;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.graceful.place.search.adapter.out.api.PlaceSearchApiRequest;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoPlaceSearchApiRequest implements PlaceSearchApiRequest {

	private String keyword;

	public static KakaoPlaceSearchApiRequest valueOf(String keyword) {
		return new KakaoPlaceSearchApiRequest(keyword);
	}
}
