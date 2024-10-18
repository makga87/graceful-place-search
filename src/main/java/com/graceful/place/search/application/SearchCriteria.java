package com.graceful.place.search.application;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchCriteria {
	private String keyword;
	private Integer page;
	private Integer size;

}
