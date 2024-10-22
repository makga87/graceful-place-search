package com.graceful.place.search.adapter.in.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KeywordSearchResponse {
	private String keyword;
	private Long searchCount;

	public static KeywordSearchResponse of(String keyword, Long searchCount) {
		return new KeywordSearchResponse(keyword, searchCount);
	}
}
