package com.graceful.place.search.application.service;


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.graceful.place.search.adapter.out.api.PlaceSearchApiFactory;
import com.graceful.place.search.adapter.out.api.PlaceSearchApiRequest;
import com.graceful.place.search.adapter.out.api.PlaceSearchApiResponse;
import com.graceful.place.search.adapter.out.api.kakao.KakaoPlaceSearchApiRequest;
import com.graceful.place.search.adapter.out.api.naver.NaverPlaceSearchApiRequest;
import com.graceful.place.search.application.SearchCriteria;
import com.graceful.place.search.application.mapper.KakaoPlaceMapper;
import com.graceful.place.search.application.mapper.NaverPlaceMapper;
import com.graceful.place.search.application.mapper.PlaceMapper;
import com.graceful.place.search.application.merger.PlacesMergeStrategy;
import com.graceful.place.search.application.port.in.PlaceSearchUseCase;
import com.graceful.place.search.application.slicer.PlacesSliceStrategy;
import com.graceful.place.search.config.PlaceSearchConfig;
import com.graceful.place.search.domain.Place;
import com.graceful.place.search.domain.SearchApiType;

@Slf4j
@CacheConfig(cacheNames = "PLACE_SEARCH")
@RequiredArgsConstructor
@Service
public class PlaceSearchService implements PlaceSearchUseCase {

	@Qualifier("placesMergeManager")
	private final PlacesMergeStrategy placesMergeManager;

	@Qualifier("placesSliceManager")
	private final PlacesSliceStrategy placesSliceManager;

	private final PlaceSearchApiFactory placeSearchApiFactory;

	@Qualifier("taskExecutor")
	private final Executor taskExecutor;

	private final PlaceSearchConfig config;

	private static final Map<SearchApiType, PlaceMapper<?>> mappers;

	static {
		mappers = Map.of(SearchApiType.KAKAO, KakaoPlaceMapper.init(),
						 SearchApiType.NAVER, NaverPlaceMapper.init());
	}

	@Cacheable(key = "#searchCriteria.keyword")
	@Override
	public List<Place> placeSearch(SearchCriteria searchCriteria) {

		CompletableFuture<List<Place>> kakaoPlaceList = getPlaces(SearchApiType.KAKAO, searchCriteria);
		CompletableFuture<List<Place>> naverPlaceList = getPlaces(SearchApiType.NAVER, searchCriteria);

		List<Place> slicedNaverPlaces = placesSliceManager.slice(naverPlaceList.join(), config.getSize().getByApi());

		List<Place> kakaoPlaces = kakaoPlaceList.join();
		List<Place> slicedKakaoPlaces = placesSliceManager.slice(kakaoPlaces, kakaoPlaces.size() - slicedNaverPlaces.size());

		return placesMergeManager.merge(slicedKakaoPlaces, slicedNaverPlaces);
	}

	@SuppressWarnings("unchecked")
	private <T> CompletableFuture<List<Place>> getPlaces(SearchApiType searchApiType, SearchCriteria searchCriteria) {

		PlaceSearchApiRequest request = createPlaceSearchRequest(searchApiType, searchCriteria);
		PlaceSearchApiResponse<T> response = placeSearchApiFactory.getSearchApi(searchApiType).searchPlaces(request);

		PlaceMapper<T> mapper = (PlaceMapper<T>) mappers.get(searchApiType);

		return CompletableFuture.supplyAsync(() -> response.getResults().stream()
														   .filter(Objects::nonNull)
														   .map(mapper::toPlace)
														   .collect(Collectors.toList()),
											 taskExecutor)
								.exceptionally(ex -> {
									log.error("Error occurred request {} API", searchApiType.name(), ex);
									return List.of();
								});
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
