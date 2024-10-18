package com.graceful.place.search.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchApiType {

	KAKAO(1),
	NAVER(0);

	private int additionalScore;


}
