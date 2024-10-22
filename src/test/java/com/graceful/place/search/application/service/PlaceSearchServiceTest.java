package com.graceful.place.search.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.doAnswer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

import com.graceful.place.search.adapter.out.api.PlaceSearchApiFactory;
import com.graceful.place.search.adapter.out.api.kakao.KakaoPlaceSearchApiResponse;
import com.graceful.place.search.adapter.out.api.naver.NaverPlaceSearchApiResponse;
import com.graceful.place.search.application.SearchCriteria;
import com.graceful.place.search.application.mapper.KakaoPlaceMapper;
import com.graceful.place.search.application.mapper.NaverPlaceMapper;
import com.graceful.place.search.application.mapper.PlaceMapper;
import com.graceful.place.search.application.merger.PlacesMergeStrategy;
import com.graceful.place.search.application.port.out.PlaceSearchPort;
import com.graceful.place.search.application.slicer.PlacesSliceStrategy;
import com.graceful.place.search.config.PlaceSearchConfig;
import com.graceful.place.search.domain.Place;
import com.graceful.place.search.domain.SearchApiType;


@ExtendWith(MockitoExtension.class)
class PlaceSearchServiceTest {
	@Mock
	private PlacesMergeStrategy placesMergeManager;

	@Mock
	private PlacesSliceStrategy placesSliceManager;

	@Mock
	private PlaceSearchApiFactory placeSearchApiFactory;

	@Mock
	private PlaceSearchPort kakaoSearchAdapter;

	@Mock
	private PlaceSearchPort naverSearchAdapter;

	@Mock
	private Executor taskExecutor;

	@Mock
	private PlaceSearchConfig config;

	private final Map<SearchApiType, PlaceMapper<?>> mappers = Map.of(SearchApiType.KAKAO, KakaoPlaceMapper.init(),
																	  SearchApiType.NAVER, NaverPlaceMapper.init());

	@InjectMocks
	private PlaceSearchService placeSearchService;

	@BeforeEach
	void setup() {

		doAnswer(invocation -> {
			Runnable runnable = invocation.getArgument(0);
			runnable.run();
			return null;
		}).when(taskExecutor).execute(any());

		when(placeSearchApiFactory.getSearchApi(SearchApiType.KAKAO)).thenReturn(kakaoSearchAdapter);
		when(placeSearchApiFactory.getSearchApi(SearchApiType.NAVER)).thenReturn(naverSearchAdapter);

		PlaceSearchConfig.Size size = mock(PlaceSearchConfig.Size.class);
		when(size.getByApi()).thenReturn(5);
		when(config.getSize()).thenReturn(size);
	}

	@DisplayName("카카오 장소명 검색 API가 실패하고, 네이버 API 성공 시, 네이버 API 응답 값만 반환한다.")
	@Test
	void testKakaoFailNaverSuccess() {
		// given
		SearchCriteria criteria = SearchCriteria.builder()
												.keyword("짬뽕")
												.size(5)
												.build();

		NaverPlaceSearchApiResponse naverResponse = mockNaverResponse();

		when(kakaoSearchAdapter.searchPlaces(any())).thenThrow(new RestClientException("API 호출 중, HttpClient 라이브러리 레벨에서 실패"));

		when(naverSearchAdapter.searchPlaces(any())).thenReturn(naverResponse);

		when(placesSliceManager.slice(any(), anyInt())).thenReturn(mockNaverPlaces());
		when(placesMergeManager.merge(any(), any())).thenReturn(mockNaverPlaces());

		// when
		List<Place> result = placeSearchService.placeSearch(criteria);

		// then
		assertEquals(result.size(), 1);
	}

	@DisplayName("네이버 장소명 검색 API가 실패하고, 카카오 API 성공 시, 카카오 API 응답 값만 반환한다.")
	@Test
	void testNaverFailKakaoSuccess() {
		// given
		SearchCriteria criteria = SearchCriteria.builder()
												.keyword("짬뽕")
												.size(5)
												.build();

		KakaoPlaceSearchApiResponse kakaoResponse = mockKakaoResponse();

		when(naverSearchAdapter.searchPlaces(any())).thenThrow(new RestClientException("API 호출 중, HttpClient 라이브러리 레벨에서 실패"));
		when(kakaoSearchAdapter.searchPlaces(any())).thenReturn(kakaoResponse);

		when(placesSliceManager.slice(any(), anyInt())).thenReturn(mockKakaoPlaces());
		when(placesMergeManager.merge(any(), any())).thenReturn(mockKakaoPlaces());

		// when
		List<Place> result = placeSearchService.placeSearch(criteria);

		// then
		assertEquals(result.size(), 1);
	}

	@DisplayName("두 API 모두 호출 실패 시, 빈 배열을 반환한다. ")
	@Test
	void testAllFail() {
		// given
		SearchCriteria criteria = SearchCriteria.builder()
												.keyword("짬뽕")
												.size(5)
												.build();

		when(naverSearchAdapter.searchPlaces(any())).thenThrow(new RestClientException("API 호출 중, HttpClient 라이브러리 레벨에서 실패"));
		when(kakaoSearchAdapter.searchPlaces(any())).thenThrow(new RestClientException("API 호출 중, HttpClient 라이브러리 레벨에서 실패"));

		when(placesSliceManager.slice(any(), anyInt())).thenReturn(Collections.emptyList());
		when(placesMergeManager.merge(any(), any())).thenReturn(Collections.emptyList());

		// when
		List<Place> result = placeSearchService.placeSearch(criteria);

		// then
		assertTrue(result.isEmpty());
	}


	private KakaoPlaceSearchApiResponse mockKakaoResponse() {
		KakaoPlaceSearchApiResponse response = mock(KakaoPlaceSearchApiResponse.class);
		when(response.getResults()).thenReturn(List.of(KakaoPlaceSearchApiResponse.Document.builder()
																						   .placeName("카뱅 건물")
																						   .addressName("판교역")
																						   .roadAddressName("판교역로")
																						   .x("127.1234567")
																						   .y("37.67890")
																						   .build()));
		return response;
	}

	private NaverPlaceSearchApiResponse mockNaverResponse() {
		return NaverPlaceSearchApiResponse.builder()
										  .items(List.of(NaverPlaceSearchApiResponse.Item.builder()
																						 .title("네이버 건물")
																						 .address("정자동")
																						 .roadAddress("정자로")
																						 .mapx("127.1234567")
																						 .mapy("37.67890")
																						 .build()))
										  .build();
	}

	private List<Place> mockKakaoPlaces() {
		List<Place> places = new ArrayList<>();
		places.add(Place.builder()
						.placeName("카뱅 건물")
						.priorityScore(1)
						.searchApiType(SearchApiType.KAKAO)
						.build());
		return places;
	}

	private List<Place> mockNaverPlaces() {
		List<Place> places = new ArrayList<>();
		places.add(Place.builder()
						.placeName("네이버 건물")
						.priorityScore(0)
						.searchApiType(SearchApiType.NAVER)
						.build());
		return places;
	}
}