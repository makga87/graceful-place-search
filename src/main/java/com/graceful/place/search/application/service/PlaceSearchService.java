package com.graceful.place.search.application.service;


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.graceful.place.search.adapter.out.api.PlaceSearchApiFactory;
import com.graceful.place.search.adapter.out.api.PlaceSearchApiRequest;
import com.graceful.place.search.adapter.out.api.PlaceSearchApiResponse;
import com.graceful.place.search.adapter.out.api.kakao.KakaoPlaceSearchApiRequest;
import com.graceful.place.search.adapter.out.api.naver.NaverPlaceSearchApiRequest;
import com.graceful.place.search.application.SearchCriteria;
import com.graceful.place.search.application.mapper.KakaoPlaceMapper;
import com.graceful.place.search.application.mapper.NaverPlaceMapper;
import com.graceful.place.search.application.mapper.PlaceMapper;
import com.graceful.place.search.application.port.in.PlaceSearchUseCase;
import com.graceful.place.search.domain.Place;
import com.graceful.place.search.domain.Places;
import com.graceful.place.search.domain.SearchApiType;

@RequiredArgsConstructor
@Service
public class PlaceSearchService implements PlaceSearchUseCase {

	private final PlaceSearchApiFactory placeSearchApiFactory;
	private static final Map<SearchApiType, PlaceMapper<?>> mappers;

	static {
		mappers = Map.of(SearchApiType.KAKAO, KakaoPlaceMapper.init(),
						 SearchApiType.NAVER, NaverPlaceMapper.init());
	}

	@Override
	public Places placeSearch(SearchCriteria searchCriteria) {

		List<Place> kakaoPlaces = getPlaces(SearchApiType.KAKAO, searchCriteria);
		List<Place> naverPlaces = getPlaces(SearchApiType.NAVER, searchCriteria);

		Places places = Places.from(kakaoPlaces);
		places.merge(naverPlaces);

		return places;
	}

	@SuppressWarnings("unchecked")
	private <T> List<Place> getPlaces(SearchApiType searchApiType, SearchCriteria searchCriteria) {

		PlaceSearchApiRequest request = createPlaceSearchRequest(searchApiType, searchCriteria);
		PlaceSearchApiResponse<T> response = placeSearchApiFactory.getSearchApi(searchApiType)
																  .searchPlaces(request);

		PlaceMapper<T> mapper = (PlaceMapper<T>) mappers.get(searchApiType);

		return response.getResults().stream()
					   .filter(Objects::nonNull)
					   .map(mapper::toPlace)
					   .collect(Collectors.toList());


	}

	private PlaceSearchApiRequest createPlaceSearchRequest(SearchApiType searchApiType, SearchCriteria searchCriteria) {
		switch (searchApiType) {
			case KAKAO:
				return KakaoPlaceSearchApiRequest.valueOf(searchCriteria.getKeyword());
			case NAVER:
				return NaverPlaceSearchApiRequest.of(searchCriteria.getKeyword(), searchCriteria.getSize());
			default:
				throw new IllegalArgumentException("Not support api type");
		}
	}
}
